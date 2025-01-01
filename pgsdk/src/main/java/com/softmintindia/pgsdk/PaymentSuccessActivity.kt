package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.pgsdk.Utils.copyToClipboard
import com.softmintindia.pgsdk.ui.theme.AppTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PaymentSuccessActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the success message passed from the intent or use a default message
//        val successMessage =
//            intent.getStringExtra("SUCCESS_MESSAGE") ?: "Payment completed successfully!"
        //        val payeeName = intent.getStringExtra("PAYEE_NAME") ?: "Unknown Payee"
//        val amount = intent.getStringExtra("AMOUNT") ?: "N/A"
//        val date = intent.getStringExtra("DATE") ?: "N/A"
//        val time = intent.getStringExtra("TIME") ?: "N/A"
//        val txnId = intent.getStringExtra("TXN_ID") ?: "N/A"



        // Get the current date and time
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")


        val successMessage = intent.getStringExtra("REMARK") ?: "Payment completed successfully!"
        val payeeName = intent.getStringExtra("PAYEE_NAME") ?: "Unknown Payee"
        val amount = intent.getStringExtra("AMOUNT") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "2025-01-01"
        val time = intent.getStringExtra("TIME") ?: "06:10:23 PM"
        val txnId = intent.getStringExtra("TXN_ID") ?: "N/A"
        val rrn = intent.getStringExtra("RRN") ?: "N/A"


        // Set the status bar and navigation bar colors to a specific shade of blue
        window.statusBarColor = Color(0xFF3F51B5).toArgb()
        window.navigationBarColor = Color(0xFF3F51B5).toArgb()

        // Enable edge-to-edge display on supported devices
        enableEdgeToEdge()

        // Automatically finish the activity after 10 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            // Uncomment the following line to finish the activity
//            finish()
        }, 10000)

        // Set the UI content of the activity using Jetpack Compose
        setContent {
            AppTheme {
                // Create a scaffold structure with a top app bar and dynamic container color
                Scaffold(containerColor = Color(0xFF3F51B5), // Set a fixed container color
                    topBar = { AppBar() } // Add the custom AppBar at the top
                ) {
                    // Center content inside a Box layout
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // Align content to the center
                    ) {
                        // Timer state to keep track of remaining seconds
                        val timerState = remember { mutableIntStateOf(10) }

                        // Launch a coroutine to decrement the timer every second
                        LaunchedEffect(Unit) {
                            while (timerState.intValue > 0) {
                                delay(1000) // Wait for 1 second
                                timerState.intValue -= 1 // Decrement the timer
                            }
                            // Uncomment to finish the activity after timer ends
                             finish()
                        }

                        // Column to arrange the UI elements vertically
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()) // Allow scrolling if needed
                                .padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(128.dp)) // Add empty space at the top
                            Text(
                                text = "You will be redirected in ${timerState.intValue} seconds..",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(8.dp)) // Add space
                            Text(
                                text = "Payment Successful!",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.background // Adapt color to theme
                            )
                            Spacer(modifier = Modifier.height(48.dp)) // Add space
                            Image(
                                painter = painterResource(id = R.drawable.ic_success), // Show a success icon
                                contentDescription = "Success Icon",
                                modifier = Modifier.size(120.dp)
                            )
                            Spacer(modifier = Modifier.weight(1f)) // Add flexible space at the bottom

                            // Display payment details in a Card layout
                            Card {
                                Column {
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column {
                                            Text(
                                                text = payeeName, // Merchant name
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.DarkGray
                                            )
                                            Spacer(modifier = Modifier.height(4.dp)) // Add space
                                            Text(
                                                text = "$date  $time",
                                                fontSize = 16.sp,
                                                color = Color.Gray // Display date and time
                                            )
//                                            Text(
//                                                text = "${currentDateTime.format(timeFormatter)}  ${currentDateTime.format(dateFormatter)}",
//                                                fontSize = 16.sp,
//                                                color = Color.Gray // Display date and time
//                                            )
                                        }
                                        Text(
                                            text = "₹ $amount", // Amount
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                    }
                                    HorizontalDivider() // Add a horizontal divider
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, top = 24.dp, bottom = 0.dp)
                                    ) {
                                        Text(
                                            text = "TXN ID:", // Transaction ID label
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.width(8.dp)) // Add space
                                        Text(
                                            text = txnId, // Display transaction ID
                                            fontSize = 16.sp, color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.width(16.dp)) // Add space

                                        // Add an icon to copy the UPI string to the clipboard
                                        Image(painter = painterResource(id = R.drawable.ic_copy),
                                            contentDescription = "Copy UPI",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    copyToClipboard(this@PaymentSuccessActivity, "TXN ID", txnId)
                                                }
                                        )
                                    }
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(start = 24.dp, bottom = 24.dp, top = 12.dp)
                                    ) {
                                        Text(
                                            text = "RRN:", // Transaction ID label
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.width(8.dp)) // Add space
                                        Text(
                                            text = rrn, // Display transaction ID
                                            fontSize = 16.sp, color = Color.DarkGray
                                        )
                                        Spacer(modifier = Modifier.width(16.dp)) // Add space

                                        // Add an icon to copy the UPI string to the clipboard
                                        Image(painter = painterResource(id = R.drawable.ic_copy),
                                            contentDescription = "Copy UPI",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
                                                    copyToClipboard(this@PaymentSuccessActivity, "RRN", rrn)
                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Custom AppBar for the top of the screen
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar() {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF3F51B5), // Background color for AppBar
                titleContentColor = Color.White // Title text color
            ), title = {}, scrollBehavior = scrollBehavior
        )
    }
}

//// Function to copy a UPI string to the clipboard
//fun com.softmintindia.pgsdk.Utils.copyToClipboard(context: Context, label:String, value: String) {
//    // Get the clipboard manager
//    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//    // Create a new clip with the UPI string
//    val clip = ClipData.newPlainText(label, value)
//    // Set the clip in the clipboard
//    clipboard.setPrimaryClip(clip)
//    // Show a toast message to notify the user
//    Toast.makeText(context, "$label copied to clipboard", Toast.LENGTH_SHORT).show()
//}
