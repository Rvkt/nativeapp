package com.softmintindia.app

//import com.softmintindia.pgsdk.PGSDKManager.showInitializationDialog
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.app.presentation.InstalledAppsList
import com.softmintindia.app.presentation.showToast
import com.softmintindia.app.ui.theme.AppTheme
import com.softmintindia.pgsdk.PGSDKManager
import com.softmintindia.pgsdk.PaymentFailedActivity
import com.softmintindia.pgsdk.PaymentSuccessActivity
import com.softmintindia.pgsdk.R
import kotlinx.coroutines.delay


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
                                        amount = "1",
                                        remark = "SDK Initialization",
                                        identifier = "GANPATI001",
                                        orderId = "TXN20241230174410"
                                    ) { success, message ->
                                        if (success) {
//                                            this@MainActivity.finish()
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
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Navigate to Payment", modifier = Modifier.padding(16.dp)
                                )
                            }

//                            LoginButtons(
//                                onLoginClick = {
//                                    this@MainActivity.makePostApiCall(
//                                        userName = "9999726418",
//                                        password = "",
//                                        source = "SDK",
//                                        mode = "VIA_MOBILE",
//                                        otp = ""
//                                    )
//                                },
//                                onSubmitOtpClick = {
//                                    this@MainActivity.submitOtp(
//                                        userName = "9999726418",
//                                        password = "",
//                                        source = "SDK",
//                                        mode = "VIA_MOBILE",
//                                        otp = "111111"
//                                    )
//                                }
//                            )


//                            PaymentScreen()


                            InstalledAppsList()

                        }
                    }
                }
            }
        }
    }


//    private fun makePostApiCall(
//        userName: String,
//        password: String,
//        source: String,
//        mode: String,
//        otp: String
//    ) {
//        // Request body map
//        val requestBody = mapOf(
//            "userName" to userName,
//            "password" to password,
//            "source" to source,
//            "mode" to mode,
//            "otp" to otp
//        )
//
//        val header = ApiHeaders.deviceIdHeader(context = this@MainActivity).toString()
//
//        val callback = ApiCallback<AuthenticationResponse> { success, data, message ->
//            if (success) {
//                Log.d("Response", "Success: $data")
//
//                if (data != null) {
//                    if (data.status.toInt() == 6) {
//                        Log.d("ApiCallback", "makePostApiCall: ${data.message}")
//                    } else if (data.status.toInt() == 1) {
//                        Log.d("ApiCallback", "makePostApiCall Submit OTP: ${data.message}")
//                    }
//                }
//
//            } else {
//                Log.e("Error", "Failure: $message")
//            }
//        }
//
//
//        ApiClient.apiService.authenticateUser(header, requestBody = requestBody)
//            .enqueue(object : Callback<AuthenticationResponse> {
//                override fun onResponse(
//                    call: Call<AuthenticationResponse>,
//                    response: Response<AuthenticationResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        responseBody?.let {
//                            Log.d("Authenticate User", "\n$it")
//                            callback.onSuccess(it)
//                        }
//                    } else {
//                        callback.onError("Response unsuccessful: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<AuthenticationResponse>, t: Throwable) {
//                    callback.onError("Network call failed: ${t.message}")
//                }
//            })
//    }

//    private fun submitOtp(
//        userName: String,
//        password: String,
//        source: String,
//        mode: String,
//        otp: String
//    ) {
//        // Request body map
//        val requestBody = mapOf(
//            "userName" to userName,
//            "password" to password,
//            "source" to source,
//            "mode" to mode,
//            "otp" to otp
//        )
//
//        val header = ApiHeaders.deviceIdHeader(context = this@MainActivity).toString()
//
//        val callback = ApiCallback<AuthenticatedResponse> { success, data, message ->
//            if (success) {
//                Log.d("Response", "Success: $data")
//
//                if (data != null) {
//                    if (data.status.toInt() == 6) {
//                        Log.d("ApiCallback", "makePostApiCall: ${data.message}")
//                    } else if (data.status.toInt() == 1) {
//                        Log.d("ApiCallback", "makePostApiCall Submit OTP: ${data.message}")
//                    }
//                }
//
//            } else {
//                Log.e("Error", "Failure: $message")
//            }
//        }
//
//
//        ApiClient.apiService.verifyUser(header, requestBody = requestBody)
//            .enqueue(object : Callback<AuthenticatedResponse> {
//                override fun onResponse(
//                    call: Call<AuthenticatedResponse>,
//                    response: Response<AuthenticatedResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        responseBody?.let {
//                            Log.d("Authenticate User", "\n$it")
//                            callback.onSuccess(it)
//                        }
//                    } else {
//                        callback.onError("Response unsuccessful: ${response.errorBody()?.string()}")
//                    }
//                }
//
//
//                override fun onFailure(call: Call<AuthenticatedResponse>, t: Throwable) {
//                    callback.onError("Failed: ${t.message}")
//                }
//            })
//    }

//    private fun initiateUpiTxn(
//        amount: String,
//        remark: String,
//        identifier: String,
//        orderId: String,
//    ) {
//        // Request body map
//        val requestBody = PgsdkInitRequest(amount, remark, identifier, orderId);
//
//        val header = ApiHeaders.withToken(context = this@MainActivity)
//
//        val callback = ApiCallback<PgsdkInitResponse> { success, data, message ->
//            if (success) {
//                Log.d("Response", "Success: $data")
//
//                if (data != null) {
//                    if (data.status.toInt() == 6) {
//                        Log.d("ApiCallback", "makePostApiCall: ${data.message}")
//                    } else if (data.status.toInt() == 1) {
//                        Log.d("ApiCallback", "makePostApiCall Submit OTP: ${data.data}")
//                    }
//                }
//
//            } else {
//                Log.e("Error", "Failure: $message")
//            }
//        }
//
//
//        ApiClient.apiService.pgsdkInitialize(header, requestBody = requestBody)
//            .enqueue(object : Callback<PgsdkInitResponse> {
//                override fun onResponse(
//                    call: Call<PgsdkInitResponse>,
//                    response: Response<PgsdkInitResponse>
//                ) {
//                    if (response.isSuccessful) {
//                        val responseBody = response.body()
//                        responseBody?.let {
//                            Log.d("Authenticate User", "\n$it")
//                            callback.onSuccess(it)
//                        }
//                    } else {
//                        callback.onError("Response unsuccessful: ${response.errorBody()?.string()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<PgsdkInitResponse>, t: Throwable) {
//                    callback.onError("Network call failed: ${t.message}")
//                }
//            })
//    }

}


@SuppressLint("DefaultLocale")
@Composable
fun DismissibleAlertDialog(
    onDismiss: () -> Unit,
    companyName: String,
    payingTo: String,
    amount: String,
) {
    var timeLeft by remember { mutableIntStateOf(10) }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        }
        onDismiss() // Close dialog after 1 minute
    }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { /* Do nothing to prevent dismissing on outside clicks */ },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {

                Image(
                    painter = painterResource(id = R.drawable.ic_softmint),
                    contentDescription = null,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Payment Confirmation",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 16.sp, fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "This will only take a moment.",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp, fontStyle = FontStyle.Italic
                    ),
//                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(32.dp))
//
//                // Row containing "Paying to" and "Amount"
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .wrapContentHeight(),
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = "Paying to",
//                            style = MaterialTheme.typography.labelSmall
//                        )
//                        Text(
//                            text = payingTo,
//                            style = MaterialTheme.typography.labelLarge.copy(
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            ),
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                    Column(
//                        modifier = Modifier
//                            .weight(1f)
//                            .wrapContentHeight(),
//                        horizontalAlignment = Alignment.End,
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(
//                            text = "Amount",
//                            style = MaterialTheme.typography.labelSmall
//                        )
//                        Text(
//                            text = "₹ $amount",
//                            style = MaterialTheme.typography.labelLarge.copy(
//                                fontWeight = FontWeight.Bold,
//                                fontSize = 16.sp
//                            ),
//                            color = MaterialTheme.colorScheme.primary
//                        )
//                    }
//                }

                // Horizontal Divider

            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {

//                Text(
//                    text = "Confirming you payment",
//                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp),
//                    textAlign = TextAlign.Center
//                )


//                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Column for Minutes
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .weight(1f) // Distribute space evenly
                    ) {
                        Text(
                            text = String.format("%02d", timeLeft / 60), // Remaining minutes
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Minutes", style = MaterialTheme.typography.labelSmall
                        )
                    }

                    // Colon separator
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Column for Seconds
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f) // Distribute space evenly
                    ) {
                        Text(
                            text = String.format("%02d", timeLeft % 60), // Remaining seconds
                            style = MaterialTheme.typography.displaySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Seconds", style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // Powered by UPI
//                Spacer(modifier = Modifier.height(64.dp))
//                Spacer(modifier = Modifier.weight(1f))

//                Text(
//                    text = "POWERED BY",
//                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
//                    textAlign = TextAlign.Center
//                )
//                Image(
//                    painter = painterResource(id = R.drawable.ic_upi),
//                    contentDescription = null,
//                    modifier = Modifier
//                        .size(48.dp)
//                        .wrapContentHeight()
//                        .fillMaxWidth()
//                )
            }
        },
        confirmButton = { /* No confirm button */ },
        dismissButton = { /* No dismiss button */ },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)) // Custom shape
    )

}


@Composable
fun LoginButtons(
    onLoginClick: () -> Unit,
    onSubmitOtpClick: () -> Unit,
) {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Button(
            onClick = onLoginClick, modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Login User",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onSubmitOtpClick, modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Submit OTP",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
fun PaymentScreen() {
    var showDialog by remember { mutableStateOf(false) }
    var dialogType by remember { mutableStateOf("SUCCESS") }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        PaymentButtons(onPaymentSuccess = {
            dialogType = "SUCCESS"
            showDialog = true
        }, onPaymentFailure = {
            dialogType = "FAILURE"
            showDialog = true
        })

        // Show DismissibleAlertDialog based on dialogType
        if (showDialog) {
            DismissibleAlertDialog(
                onDismiss = {
                    // Handle dismissal based on dialog type
                    val message =
                        if (dialogType == "SUCCESS") "Payment Successful" else "Payment Failed"
                    val activityClass =
                        if (dialogType == "SUCCESS") PaymentSuccessActivity::class.java else PaymentFailedActivity::class.java

                    // Prepare details
                    val payeeName = "John Doe"
                    val amount = "500"
                    val date = "26 Dec, 2024"
                    val time = "10:30 AM"
                    val txnId = "TXN20241226"

                    // Show toast message
                    showToast(context, message)

                    // Navigate to the appropriate activity
                    val intent = Intent(context, activityClass).apply {
                        putExtra("SUCCESS_MESSAGE", message)
                        putExtra("PAYEE_NAME", payeeName)
                        putExtra("AMOUNT", amount)
                        putExtra("DATE", date)
                        putExtra("TIME", time)
                        putExtra("TXN_ID", txnId)
                    }
                    context.startActivity(intent)

                    // Close the dialog
                    showDialog = false
                }, companyName = "Softmint Technologies", payingTo = "John Doe", amount = "500"
            )
        }
    }
}

@Composable
fun PaymentButtons(
    onPaymentSuccess: () -> Unit,
    onPaymentFailure: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()
    ) {
        Button(onClick = onPaymentSuccess) {
            Text("Simulate Success")
        }
        Button(onClick = onPaymentFailure) {
            Text("Simulate Failure")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        InstalledAppsList()
    }
}

