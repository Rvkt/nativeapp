package com.softmintindia.pgsdk.ui.theme

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.pgsdk.R

class PaymentSuccessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                PaymentSuccessScreen(
                    onBackPressed = { onBackPressed() },
                    transactionId = "TX123456789",
                    amount = "₹1000"
                )
            }
        }
    }
}

@Composable
fun PaymentSuccessScreen(
    onBackPressed: () -> Unit,
    transactionId: String,
    amount: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Icon
        val successIcon: Painter = painterResource(id = R.drawable.ic_default)  // Replace with your own icon
        Image(
            painter = successIcon,
            contentDescription = "Payment Success",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Success message
        Text(
            text = "Payment Successful!",
            style = MaterialTheme.typography.h5,
            color = Color(0xFF4CAF50), // Green color for success
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // Transaction Details
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Transaction ID: $transactionId",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Text(
                text = "Amount: $amount",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Button to go back
        Button(
            onClick = onBackPressed,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Go Back",
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentSuccessPreview() {
    AppTheme {
        PaymentSuccessScreen(
            onBackPressed = {},
            transactionId = "TX123456789",
            amount = "₹1000"
        )
    }
}
