package com.softmintindia.pgsdk.data.api


import android.content.Context
import android.util.Log
import com.softmintindia.pgsdk.NetworkUtils
import com.softmintindia.pgsdk.domain.PgSdkInitResponse
import com.softmintindia.pgsdk.domain.PgSdkInitResponseData

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NetworkCall {

    private const val TAG = "NetworkCall"

    // Function to initiate a network call for the payment API
    fun initiatePaymentApiCall(
        context: Context,
        amount: String,
        remark: String,
        identifier: String,
        orderId: String,
        token: String,
        callback: (Boolean, String, PgSdkInitResponseData?) -> Unit
    ) {
        // Check for network connectivity
        if (!isNetworkConnected(context)) {
            callback(false, "No internet connection.", null)
            return
        }

        // Prepare the API request and header
        val requestBody = ApiRequests.PgsdkInitRequest(amount, remark, identifier, orderId)
        val header = ApiHeaders.withToken(context, token)

        // Make the API call using Retrofit
        ApiClient.apiService.pgsdkInitialize(header, requestBody = requestBody)
            .enqueue(object : Callback<PgSdkInitResponse> {
                override fun onResponse(
                    call: Call<PgSdkInitResponse>,
                    response: Response<PgSdkInitResponse>
                ) {
                    Log.d(TAG, "Response body:\n${response.body()}")
                    Log.d(TAG, "Response Status:\n${response.body()?.status}")

                    if (response.isSuccessful) {
                        val responseBody = response.body()

                        // Check the response status
                        val responseStatus = responseBody?.status

                        if (responseStatus == 1L) { // Assuming 1L indicates success
                            // Handle success logic
                            responseBody.let {
                                callback(true, "Success: ${it.data.remark}", it.data)
                            }
                        } else {
                            // Handle failure logic
                            responseBody?.let {
                                callback(false, "Failed: ${it.message}", it.data)
                            } ?: callback(false, "Failed: No response data", null)
                        }
                    } else {
                        callback(false, "Error: ${response.message()}", null)
                    }
                }

                override fun onFailure(call: Call<PgSdkInitResponse>, t: Throwable) {
                    callback(false, "Error: ${t.message}", null)
                    Log.e(TAG, "API Call failed: ${t.message}")
                }
            })
    }


    // Helper function to check network connectivity
    private fun isNetworkConnected(context: Context): Boolean {
        return NetworkUtils.isConnected(context)
    }
}