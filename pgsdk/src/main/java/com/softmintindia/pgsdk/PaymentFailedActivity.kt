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
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.pgsdk.ui.theme.AppTheme
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class PaymentFailedActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val successMessage = intent.getStringExtra("SUCCESS_MESSAGE") ?: "Payment completed successfully!"

        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

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
                    containerColor = Color(0xFF3F51B5),
//                    containerColor = MaterialTheme.colorScheme.background, // Adapts to light/dark theme
                    topBar = { AppBar()}
                ) {
                    // Box to center the content
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // This centers the content
                    ) {

                        val timerState = remember { mutableStateOf(10) } // Initial 10 seconds timer

                        // LaunchedEffect will update the timerState every second
                        LaunchedEffect(Unit) {
                            while (timerState.value > 0) {
                                delay(1000) // Wait for 1 second
                                timerState.value -= 1
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
                                text = "You will be redirected in ${timerState.value} seconds..",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                color = Color.White,
                                textAlign = TextAlign.Start
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Payment Failed!",
                                fontSize = 24.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.background // Color adapts to light/dark mode
                            )
                            Spacer(modifier = Modifier.height(48.dp))
                            Image(
                                painter = painterResource(id = R.drawable.ic_failed), // Replace with your image resource
                                contentDescription = "Copy UPI",
                                modifier = Modifier
                                    .size(120.dp)
                            )
//                            Spacer(modifier = Modifier.height(16.dp))
//                            Text(
//                                text = successMessage,
//                                fontSize = 16.sp,
//                                color = MaterialTheme.colorScheme.onBackground // Color adapts to light/dark mode
//                            )
                            Spacer(modifier = Modifier.weight(1f))

                            Card {
                                Column {
                                    Row (
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ){
                                        Column {
                                            Text(text = "ACPL", fontSize = 20.sp,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.DarkGray,
                                                textAlign = TextAlign.Start)
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "${currentDateTime.format(timeFormatter)}  ${currentDateTime.format(dateFormatter)}",
                                                fontSize = 16.sp,
                                                fontFamily = FontFamily.SansSerif,
                                                fontWeight = FontWeight.Normal,
                                                color = Color.Gray,
                                                textAlign = TextAlign.Start
                                            )
                                        }
                                        Text(text = "₹ 500", fontSize = 20.sp,
                                            fontFamily = FontFamily.Monospace,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.End)
                                    }
                                    HorizontalDivider()
                                    Row (
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(24.dp),){
                                        Text(
                                            text = "TXN ID:",
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Start
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "TXN20241226",
                                            fontSize = 16.sp,
                                            fontFamily = FontFamily.SansSerif,
                                            fontWeight = FontWeight.Normal,
                                            color = Color.DarkGray,
                                            textAlign = TextAlign.Start
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        // IconButton to copy UPI to clipboard
//                                    IconButton(
//                                        onClick = { copyToClipboard(this@PaymentSuccessActivity, "upiString") }
//                                    ) {
//                                        Icon(
//                                            imageVector = Icons.Outlined.Add,
//                                            contentDescription = "Copy UPI",
//                                            tint = Color.Gray
//                                        )
//                                    }

                                        Image(
                                            painter = painterResource(id = R.drawable.ic_copy), // Replace with your image resource
                                            contentDescription = "Copy UPI",
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clickable {
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

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun AppBar() {
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF3F51B5),
                titleContentColor = Color.White,
            ),
            title = {
            },
            scrollBehavior = scrollBehavior
        )
    }

}
