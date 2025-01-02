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
import androidx.compose.foundation.background
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
import com.softmintindia.pgsdk.utils.DismissibleAlertDialog
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
                        Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                            Button(
                                onClick = {
                                    PGSDKManager.initialize(
                                        context = this@MainActivity,
                                        token = "U8lhxHPSNei90rUSaVebMC11fRyF1MLWrmTip+1yjInOO16/hbJVKf5f/QbRiIp69oHZ1lxqMLkq0aiwuAwtvfKWzCid83Y5zKPR4TaS3FTFgCEC+fe5vC5dTuLx6FzmYvupZRRJs1xVmTmjv8zW3alueclL8DCoesY+QOco4Eb7EmstLPdVsjmW8LDZNxDXh5ZK/ZRKSG4AKxt5wits6f9CpuEGU/VeO1mNCSTARxoF8ioaac/3jFyYWkrHz9HJ0q0D/T7tGHZhb/MnUogxS+vEN3QJtg6CaV0/Y8lEK0srfBz/EOzJDtPMi8IRoW+2VEjwsHoS3PMtjzlincbvRg==",
                                        amount = "10",
                                        remark = "SDK Initialization",
                                        identifier = "GANPATI001",
                                        orderId = generateOrderId()
                                    ) { success, message ->
                                        if (success) {
                                            // this@MainActivity.finish()
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
                                    .background(MaterialTheme.colorScheme.background)
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


                            PaymentScreen()
//                            InstalledAppsList()

                        }
                    }
                }
            }
        }
    }

    @SuppressLint("DefaultLocale")
    fun generateOrderId(): String {
        val currentTime = java.util.Calendar.getInstance()
        val year = currentTime.get(java.util.Calendar.YEAR)
        val day = currentTime.get(java.util.Calendar.DAY_OF_YEAR)
        val hour = currentTime.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(java.util.Calendar.MINUTE)
        val second = currentTime.get(java.util.Calendar.SECOND)

        return String.format("ORDERID%04d%03d%02d%02d%02d", year, day, hour, minute, second)
    }

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

