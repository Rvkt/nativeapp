package com.softmintindia.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.softmintindia.app.network.ApiCallback
import com.softmintindia.app.network.ApiClient
import com.softmintindia.app.network.models.AuthenticatedResponse
import com.softmintindia.app.ui.theme.AppTheme
import com.softmintindia.pgsdk.PGSDKManager
import com.softmintindia.pgsdk.PaymentFailedActivity
import com.softmintindia.pgsdk.PaymentSuccessActivity
import com.softmintindia.pgsdk.network.ApiHeaders
import com.softmintindia.pgsdk.network.models.AuthenticationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        // Use a Column to stack the button on top
                        Column(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {
                                    PGSDKManager.initialize(
                                        context = this@MainActivity,
                                        apiKey = "123456",
                                        amount = "100.00",
                                        remark = "SDK Initialization"
                                    ) { success, message ->
                                        if (success) {
                                            // Initialization successful, PaymentActivity will start automatically
                                            Log.d("MainActivity", message)
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Initialization successful",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            // Handle initialization failure
                                            Log.e("MainActivity", message)
                                            // Show a toast saying "Initialization failed"
                                            Toast.makeText(
                                                this@MainActivity,
                                                "Initialization failed, $message",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp)
                            ) {
                                Text("Go to Payment")
                            }

                            Button(
                                onClick = {
                                    this@MainActivity.makePostApiCall(
                                        userName = "9999726418",
                                        password = "",
                                        source = "SDK",
                                        mode = "VIA_MOBILE",
                                        otp = ""
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Login User",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))


                            Button(
                                onClick = {
                                    this@MainActivity.submitOtp(
                                        userName = "9999726418",
                                        password = "",
                                        source = "SDK",
                                        mode = "VIA_MOBILE",
                                        otp = "111111"
                                    )
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Submit OTP",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }



                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Button(
                                    onClick = {
                                        val successMessage = "Payment Successful"
                                        val payeeName = "John Doe" // Example payee name
                                        val amount = "500" // Example amount
                                        val date = "26 Dec, 2024" // Example date
                                        val time = "10:30 AM" // Example time
                                        val txnId = "TXN20241226" // Example transaction ID

                                        // Show a toast message
                                        showToast(this@MainActivity, successMessage)

                                        // Create an intent to start PaymentSuccessActivity
                                        val intent = Intent(
                                            this@MainActivity,
                                            PaymentSuccessActivity::class.java
                                        ).apply {
                                            putExtra("SUCCESS_MESSAGE", successMessage)
                                            putExtra("PAYEE_NAME", payeeName)
                                            putExtra("AMOUNT", amount)
                                            putExtra("DATE", date)
                                            putExtra("TIME", time)
                                            putExtra("TXN_ID", txnId)
                                        }

                                        // Start the activity
                                        this@MainActivity.startActivity(intent)
                                    },
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .weight(1f)
                                ) {
                                    Text(text = "Payment Success")
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                // Button for failure
                                Button(
                                    onClick = {
                                        val failureMessage = "Payment Failed"
                                        showToast(this@MainActivity, failureMessage)
                                        val intent = Intent(
                                            this@MainActivity,
                                            PaymentFailedActivity::class.java
                                        )
                                        intent.putExtra("FAILURE_MESSAGE", failureMessage)
                                        this@MainActivity.startActivity(intent)
                                    },
                                    modifier = Modifier
                                        .padding(vertical = 8.dp)
                                        .weight(1f)
                                ) {
                                    Text(text = "Payment Failure")
                                }
                            }

//                            InstalledAppsList()

                        }
                    }
                }
            }
        }
    }

    private fun makePostApiCall(
        userName: String,
        password: String,
        source: String,
        mode: String,
        otp: String
    ) {
        // Request body map
        val requestBody = mapOf(
            "userName" to userName,
            "password" to password,
            "source" to source,
            "mode" to mode,
            "otp" to otp
        )

        val header = ApiHeaders.deviceIdHeader(context = this@MainActivity).toString()

        val callback = ApiCallback<AuthenticationResponse> { success, data, message ->
            if (success) {
                Log.d("Response", "Success: $data")

                if (data != null) {
                    if (data.status.toInt() == 6) {
                        Log.d("ApiCallback", "makePostApiCall: ${data.message}")
                    } else if (data.status.toInt() == 1) {
                        Log.d("ApiCallback", "makePostApiCall Submit OTP: ${data.message}")
                    }
                }

            } else {
                Log.e("Error", "Failure: $message")
            }
        }


        ApiClient.apiService.authenticateUser(header, requestBody = requestBody)
            .enqueue(object : Callback<AuthenticationResponse> {
                override fun onResponse(
                    call: Call<AuthenticationResponse>,
                    response: Response<AuthenticationResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            Log.d("Authenticate User", "\n$it")
                            callback.onSuccess(it)
                        }
                    } else {
                        callback.onError("Response unsuccessful: ${response.errorBody()?.string()}")
                    }
                }

                override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
                    callback.onError("Network call failed: ${t.message}")
                }
            })
    }

    private fun submitOtp(
        userName: String,
        password: String,
        source: String,
        mode: String,
        otp: String
    ) {
        // Request body map
        val requestBody = mapOf(
            "userName" to userName,
            "password" to password,
            "source" to source,
            "mode" to mode,
            "otp" to otp
        )

        val header = ApiHeaders.deviceIdHeader(context = this@MainActivity).toString()

        val callback = ApiCallback<AuthenticatedResponse> { success, data, message ->
            if (success) {
                Log.d("Response", "Success: $data")

                if (data != null) {
                    if (data.status.toInt() == 6) {
                        Log.d("ApiCallback", "makePostApiCall: ${data.message}")
                    } else if (data.status.toInt() == 1) {
                        Log.d("ApiCallback", "makePostApiCall Submit OTP: ${data.message}")
                    }
                }

            } else {
                Log.e("Error", "Failure: $message")
            }
        }


        ApiClient.apiService.verifyUser(header, requestBody = requestBody)
            .enqueue(object : Callback<AuthenticatedResponse> {
                override fun onResponse(
                    call: Call<AuthenticatedResponse>,
                    response: Response<AuthenticatedResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        responseBody?.let {
                            Log.d("Authenticate User", "\n$it")
                            callback.onSuccess(it)
                        }
                    } else {
                        callback.onError("Response unsuccessful: ${response.errorBody()?.string()}")
                    }
                }


                override fun onFailure(call: Call<AuthenticatedResponse>, t: Throwable) {
                    callback.onError("Failed: ${t.message}")
                }
            })
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        InstalledAppsList()
    }
}

