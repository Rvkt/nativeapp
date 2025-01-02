package com.softmintindia.pgsdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import com.softmintindia.app.data.ApiImplementation
import com.softmintindia.pgsdk.data.api.ApiClient
import com.softmintindia.pgsdk.data.api.ApiHeaders
import com.softmintindia.pgsdk.data.api.ApiRequests
import com.softmintindia.pgsdk.domain.CheckTxnStatusResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentHelper {

    val TAG = "Payment Helper"
    private var txnId: String? = null

    // Function to initiate UPI payment with ActivityResultLauncher
    fun initiateUpiPayment(
        context: Context,
        amount: String,
        upiId: String,
        name: String,
        note: String,
        txnId: String,
        url: String,
        upiAppPackage: String,
        upiPaymentLauncher: ActivityResultLauncher<Intent>
    ) {
        try {

            this.txnId = txnId

            // Construct the UPI payment URL
            val upiPaymentUrl = buildUpiPaymentUrl(
                payeeAddress = upiId,
                payeeName = name,
                merchantCode = "7322",
                transactionId = txnId,
                transactionReferenceId = txnId,
                transactionNote = note,
                amount = amount,
                url = url
            )

            Log.d("UPI", "UPI Payment URL: $upiPaymentUrl")

            // Create an intent to launch the UPI app
            val upiIntent = Intent(Intent.ACTION_VIEW, Uri.parse(upiPaymentUrl))

            // Set the UPI app package in the intent
            upiIntent.setPackage(upiAppPackage)

            // Check if the UPI app is available and launch it
            val resolveInfo = context.packageManager.queryIntentActivities(upiIntent, 0)
            if (resolveInfo.isNotEmpty()) {
                // Launch UPI app using the ActivityResultLauncher
                upiPaymentLauncher.launch(upiIntent)
            } else {
                // Launch the app store to install the app
                launchAppInStore(context, upiAppPackage)

                // Show message if the specified UPI app is not found
                Toast.makeText(context, "The specified UPI app is not installed", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Failed to initiate UPI payment", Toast.LENGTH_SHORT).show()
        }
    }


    fun checkTxnStatus(
        context: Context,
        orderId: String,
        token: String,
        checkStatusCallback: (String, CheckTxnStatusResponse?) -> Unit, // Updated callback signature
    ) {
        try {
            val requestBody = ApiRequests.CheckTxnRequest(orderId)
            val header = ApiHeaders.withToken(context = context, token = token)

            val logTag: String = "$TAG: Check Txn Status"

            val callback = ApiImplementation<CheckTxnStatusResponse> { success, responseData, message ->
                // Handle API response

                if (success) {
                    val data = responseData?.data
                    Log.d(logTag, "Success: $data")

                    if (data != null) {
                        when (data.status) {
                            "SUCCESS" -> {
                                // Invoke the callback with true, message, and responseData
                                checkStatusCallback(data.remark, responseData)
                            }
                            "FAILED" -> {
                                // Invoke the callback with false, message, and responseData
                                checkStatusCallback(data.remark, responseData)
                            }
                        }
                    }
                } else {
                    Log.e(logTag, "Failure: $message")

                    // Invoke the callback with false, message, and responseData
                    if (responseData != null) {
                        checkStatusCallback(responseData.message, responseData)
                    }
                }
            }

            // Call the API
            ApiClient.apiService.checkTxnStatus(header, requestBody = requestBody)
                .enqueue(object : Callback<CheckTxnStatusResponse> {
                    override fun onResponse(
                        call: Call<CheckTxnStatusResponse>,
                        response: Response<CheckTxnStatusResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                Log.d(TAG, "\n$it")
                                callback.onSuccess(it)
                            }
                        } else {
                            callback.onError(
                                "Response unsuccessful: ${response.toString()}"
                            )
                        }
                    }

                    override fun onFailure(call: Call<CheckTxnStatusResponse>, t: Throwable) {
                        callback.onError("${t.message}")
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(context, "API call failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }



    // Function to launch the app in the Play Store
    private fun launchAppInStore(context: Context, packageName: String) {
        val playStoreIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName"))
        context.startActivity(playStoreIntent)
    }

    private fun buildUpiPaymentUrl(
        payeeAddress: String,
        payeeName: String,
        merchantCode: String,
        transactionId: String,
        transactionReferenceId: String,
        transactionNote: String,
        amount: String,
        currency: String = "INR",
        url: String
    ): String {
        return "upi://pay?pa=8757237359@ybl&pn=Ritesh&mc=4900&tid=rah67676868&tr=rah67676868&tn=test&am=10&cu=INR&mode=04&url=www.softmint.com"

//        return Uri.parse("upi://pay")
//            .buildUpon()
//            .appendQueryParameter("pa", Uri.encode(payeeAddress))  // Payee UPI ID
//            .appendQueryParameter("pn", Uri.encode(payeeName))     // Payee Name
//            .appendQueryParameter("mc", Uri.encode(merchantCode))  // Merchant Code
//            .appendQueryParameter("tid", Uri.encode(transactionId)) // Transaction ID
//            .appendQueryParameter("tr", Uri.encode(transactionReferenceId)) // Transaction Reference ID
//            .appendQueryParameter("tn", Uri.encode(transactionNote)) // Transaction Note
//            .appendQueryParameter("am", Uri.encode(amount)) // Amount
//            .appendQueryParameter("cu", Uri.encode(currency)) // Currency
//            .appendQueryParameter("url", Uri.encode(url)) // URL
//            .build()
//            .toString()

    }





    // Handle the result of the UPI payment (in your activity or composable)
    fun handleUpiPaymentResponse(
        context: Context,
        data: Intent?
    ) {
        if (data == null) {
            Toast.makeText(context, "No response received.", Toast.LENGTH_SHORT).show()
            return
        }

        val response = data.getStringExtra("response")
        if (response.isNullOrEmpty()) {
            Toast.makeText(context, "Empty response from UPI app.", Toast.LENGTH_SHORT).show()
            return
        }

        val responseMap = response.split("&")
            .mapNotNull { param ->
                val pair = param.split("=")
                if (pair.size == 2) pair[0] to pair[1] else null
            }
            .toMap()

        val responseCode = responseMap["Status"] // Extracting status from the parsed response


        when (responseCode?.uppercase()) {

            "SUCCESS" -> {
                val successMessage = "Payment Successful"

                // Show Toast for success
                showToast(context, successMessage)

//                txnId?.let { checkTxnStatus(context = context, orderId = it, checkStatusCallback = ()) }


            }
            "FAILURE" -> {
                val failureMessage = "Payment Failed"

                // Show Toast for failure
                showToast(context, failureMessage)

//                txnId?.let { checkTxnStatus(context = context, orderId = it) }

                // Navigate to PaymentFailedActivity and pass the failure message
//                val intent = Intent(context, PaymentFailedActivity::class.java)
//                intent.putExtra("FAILURE_MESSAGE", failureMessage)
//                context.startActivity(intent)
            }
            "SUBMITTED" -> {
                showToast(context, "Payment Pending")
                // todo: timeout after the 1 minute
//                Toast.makeText(context, "Payment Pending", Toast.LENGTH_SHORT).show()

//                txnId?.let { checkTxnStatus(context = context, orderId = it) }

            }
            else -> {
                showToast(context, "Payment Status Unknown")
//                Toast.makeText(context, "Payment Status Unknown", Toast.LENGTH_SHORT).show()
            }


        }
        // Log the response for debugging purposes
        Log.d("UPIResponse", "Response: $response")
        Log.d("UPIResponse", "Parsed Map: $responseMap")
    }

    // Helper method to show toast messages
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }



}

