package com.softmintindia.pgsdk

//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
//import com.softmintindia.pgsdk.utils.copyToClipboard
import com.softmintindia.pgsdk.ui.theme.AppTheme
import kotlinx.coroutines.delay

class PaymentFailedActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val successMessage =
//            intent.getStringExtra("SUCCESS_MESSAGE") ?: "Payment completed successfully!"

//        val currentDateTime = LocalDateTime.now()
//        val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
//        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

//        val payeeName = intent.getStringExtra("PAYEE_NAME") ?: "Unknown Payee"
//        val amount = intent.getStringExtra("AMOUNT") ?: "N/A"
//        val date = intent.getStringExtra("DATE") ?: "N/A"
//        val time = intent.getStringExtra("TIME") ?: "N/A"
//        val txnId = intent.getStringExtra("TXN_ID") ?: "N/A"

        val remark = intent.getStringExtra("REMARK") ?: "Payment completed successfully!"
        val payeeName = intent.getStringExtra("PAYEE_NAME") ?: "Unknown Payee"
        val amount = intent.getStringExtra("AMOUNT") ?: "N/A"
        val date = intent.getStringExtra("DATE") ?: "N/A"
        val time = intent.getStringExtra("TIME") ?: "N/A"
        val txnId = intent.getStringExtra("TXN_ID") ?: "N/A"
        val rrn = intent.getStringExtra("RRN") ?: "N/A"


        // Set system UI visibility to non-transparent
        window.statusBarColor = Color(0xFF3F51B5).toArgb()
        window.navigationBarColor = Color(0xFF3F51B5).toArgb()

        enableEdgeToEdge()

        // Start the timer to finish the activity after 10 seconds
        Handler(Looper.getMainLooper()).postDelayed({
            finish() // Finish the activity after 10 seconds
        }, 10000)

        setContent {
            AppTheme {
                // Scaffold with dynamic container color based on system theme
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background,
//                    containerColor = MaterialTheme.colorScheme.background, // Adapts to light/dark theme
                    topBar = { AppBar() }
                ) {
                    // Box to center the content
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // This centers the content
                    ) {

                        val timerState = remember { mutableIntStateOf(10) } // Initial 10 seconds timer

                        // LaunchedEffect will update the timerState every second
                        LaunchedEffect(Unit) {
                            while (timerState.intValue > 0) {
                                delay(1000) // Wait for 1 second
                                timerState.intValue -= 1
                            }
                            // Here you can add the redirection logic when the timer reaches 0
                            finish() // Finishes the activity after the timer reaches 0
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .verticalScroll(rememberScrollState()) // Enable scrolling if content is too long
                                .padding(24.dp),
//                            verticalArrangement = Arrangement.SpaceAround,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Spacer(modifier = Modifier.height(128.dp))
                            Text(
                                text = "You will be redirected in ${timerState.intValue} seconds..",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Payment Failed!",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.onBackground // Color adapts to light/dark mode
                            )
                            Spacer(modifier = Modifier.height(48.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_failed), // Replace with your image resource
                                contentDescription = "Copy UPI",
                                modifier = Modifier
                                    .size(120.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = remark,
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                            Spacer(modifier = Modifier.weight(1f))

                            PaymentDetailsCard(payeeName, date, time, amount, txnId, rrn)


                        }
                    }
                }
            }
        }
    }

    @Composable
    fun PaymentDetailsCard(
        payeeName: String,
        date: String,
        time: String,
        amount: String,
        txnId: String,
        rrn: String
    ) {
        Card(
            modifier = Modifier.padding(16.dp) // Add padding around the card
        ) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onBackground) // Set background color
                    .padding(16.dp) // Padding inside the card
            ) {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = payeeName,
                            fontSize = 20.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.background, // Text color based on theme
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$date $time",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant, // Secondary text color
                            textAlign = TextAlign.Start
                        )
                    }
                    Text(
                        text = "₹ $amount",
                        fontSize = 20.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary, // Primary color for emphasis
                        textAlign = TextAlign.End
                    )
                }
                HorizontalDivider()
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 24.dp, bottom = 0.dp)
                ) {
                    Text(
                        text = "TXN ID:", // Transaction ID label
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background // Standard text color
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add space
                    Text(
                        text = txnId, // Display transaction ID
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly lighter color
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Add space

                    // Add an icon to copy the UPI string to the clipboard
                    Image(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy UPI",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                // copyToClipboard(context, "TXN ID", txnId)
                            }
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, bottom = 24.dp, top = 12.dp)
                ) {
                    Text(
                        text = "RRN:", // RRN label
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.background // Standard text color
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Add space
                    Text(
                        text = rrn, // Display RRN
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Slightly lighter color
                    )
                    Spacer(modifier = Modifier.width(16.dp)) // Add space

                    // Add an icon to copy the UPI string to the clipboard
                    Image(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "Copy UPI",
                        modifier = Modifier
                            .size(16.dp)
                            .clickable {
                                // copyToClipboard(context, "RRN", rrn)
                            }
                    )
                }
            }
        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar() {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF3F51B5),
                titleContentColor = MaterialTheme.colorScheme.onBackground
            ),
            title = {
            },
            scrollBehavior = scrollBehavior
        )
    }

}
