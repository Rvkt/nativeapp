package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import com.softmintindia.app.data.ApiImplementation
import com.softmintindia.pgsdk.data.api.ApiClient
import com.softmintindia.pgsdk.data.api.ApiHeaders
import com.softmintindia.pgsdk.data.api.ApiRequests
import com.softmintindia.pgsdk.domain.PgsdkInitResponse
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


object PGSDKManager {
    @OptIn(DelicateCoroutinesApi::class)
    val TAG = "Payment Helper"


    fun initialize(
        context: Context,
        token: String,
        amount: String,
        remark: String,
        orderId: String,
        identifier: String,
        callback: (Boolean, String) -> Unit,
    ) {

        // Launch coroutine for asynchronous tasks
        launchInitializationCoroutine(context, amount, token, remark, orderId, identifier, callback)

    }


    private fun isNetworkConnected(context: Context): Boolean {
        return NetworkUtils.isConnected(context)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun launchInitializationCoroutine(
        context: Context,
        amount: String,
        token: String,
        remark: String,
        orderId: String,
        identifier: String,
        callback: (Boolean, String) -> Unit,
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {

                if (!isNetworkConnected(context)) {
                    callback(false, "No internet connection.")
                    return@launch
                }

                // Call the payment API and handle the response
                callPaymentApi(context,  remark, identifier, token, orderId, amount, callback)

            } catch (e: Exception) {
                Log.e(TAG, "Initialization error: ${e.message}", e)
                callback(false, "Initialization failed: ${e.message}")
            }
        }
    }

    // Updated callPaymentApi method to include qrService, raiseRequest, and intentRequest in the callback
    private fun callPaymentApi(
        context: Context,
        remark: String,
        identifier: String,
        token: String,
        orderId: String,
        amount: String,
        sdkCallBack: (Boolean, String) -> Unit,
    ) {
        try {

            val requestBody = ApiRequests.PgsdkInitRequest(amount, remark, identifier, orderId)
            val header = ApiHeaders.withToken(context = context, token = token)
            val dialog = showInitializationDialog(context)

            val callback = ApiImplementation<PgsdkInitResponse> { success, responseData, message ->
                dialog.dismiss()
                if (success) {
                    val data = responseData?.data
                    Log.d(TAG, "Success: $data")
                    if (data != null) {
                        sdkCallBack(true, data.remark)
                        startPaymentActivity(
                            context,
                            data.companyName,
                            data.amount,
                            token,
                            data.qrString,
                            data.orderId,
                            data.qrRequest,
                            data.raiseRequest,
                            data.intentRequest
                        )
                    } else {
                        showErrorActivity(context, responseData?.message.toString())
                    }
                } else {

                    Log.e(TAG, "Failed: $message")
                    if (message != null) {
                        sdkCallBack(false, message)
                        showErrorActivity(context, message)
                    }
//                    if (responseData != null) {
//                        showErrorActivity(context, responseData.message)
//                    }
                    Log.e(TAG, "Failure: $message")
                }
            }

            ApiClient.apiService.pgsdkInitialize(header, requestBody = requestBody)
                .enqueue(object : Callback<PgsdkInitResponse> {
                    override fun onResponse(
                        call: Call<PgsdkInitResponse>,
                        response: Response<PgsdkInitResponse>,
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            responseBody?.let {
                                Log.d(TAG, "\n$it")
                                callback.onSuccess(it)
                            }
                        } else {
                            callback.onError(
                                "Response unsuccessful: $response"
                            )
                        }
                    }

                    override fun onFailure(call: Call<PgsdkInitResponse>, t: Throwable) {
                        callback.onError("${t.message}")
                    }
                })
        } catch (e: Exception) {
            Toast.makeText(context, "API call failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun startPaymentActivity(
        context: Context,
        companyName: String,
        amount: String,
        token: String,
        upiUrl: String,
        orderId: String,
        qrService: Boolean,
        raiseRequest: Boolean,
        intentRequest: Boolean,
    ) {
        val intent = Intent(context, PaymentActivity::class.java).apply {
            putExtra("COMPANY", companyName)
            putExtra("AMOUNT", amount)
            putExtra("TOKEN", token)
            putExtra("UPI_URL", upiUrl)
            putExtra("ORDER_ID", orderId)
            putExtra("QR_SERVICE", qrService)
            putExtra("RAISE_REQUEST", raiseRequest)
            putExtra("INTENT_REQUEST", intentRequest)

//            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }

    private fun showErrorActivity(context: Context, errorMessage: String) {
        val intent = Intent(context, ErrorActivity::class.java).apply {
            putExtra("ERROR_MESSAGE", errorMessage)
        }
        context.startActivity(intent)
    }


    @OptIn(DelicateCoroutinesApi::class)
    private fun showInitializationDialog(context: Context): Dialog {
        val dialog = Dialog(context).apply {
            setContentView(ProgressBar(context).apply {
                isIndeterminate = true
            })
            setCancelable(false)
            show()
        }

        // Automatically dismiss the dialog and finish the activity after 30 seconds
        GlobalScope.launch(Dispatchers.Main) {
            delay(30000) // 30-second timeout
            if (dialog.isShowing) {
                dialog.dismiss()
                if (context is Activity) {
                    context.finish() // Finish the activity if it's still running
                }
            }
        }

        return dialog
    }


    // todo: Retrieve Device ID
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
}
