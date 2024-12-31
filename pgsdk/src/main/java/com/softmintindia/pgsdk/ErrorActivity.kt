package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.pgsdk.ui.theme.AppTheme


class ErrorActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val errorMessage = intent.getStringExtra("ERROR_MESSAGE") ?: "Unknown Error"

        setContent {
            AppTheme {
                // Scaffold with dynamic container color based on system theme
                Scaffold(
                    containerColor = MaterialTheme.colorScheme.background, // Adapts to light/dark theme
                    topBar = { /* AppBar() if needed */ }
                ) {
                    // Box to center the content
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center // This centers the content
                    ) {

                        val timerState = remember { mutableIntStateOf(10) }
                        LaunchedEffect(Unit) {
                            while (timerState.intValue > 0) {
                                kotlinx.coroutines.delay(1000)
                                timerState.intValue -= 1
                            }
                            finish()
                        }


                        Column(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()) // Enable scrolling if content is too long
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "You will be redirected in ${timerState.intValue} seconds..",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.SansSerif,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Start
                            )

                            Text(
                                text = "Payment Gateway Initialization Failed.",
                                fontSize = 20.sp,
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                maxLines = 2,
                                color = MaterialTheme.colorScheme.primary // Color adapts to light/dark mode
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = errorMessage,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onBackground // Color adapts to light/dark mode
                            )
                        }
                    }
                }
            }
        }
    }
}


