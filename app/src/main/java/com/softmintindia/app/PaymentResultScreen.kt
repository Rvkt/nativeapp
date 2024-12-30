package com.softmintindia.app

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.pgsdk.PaymentFailedActivity
import com.softmintindia.pgsdk.PaymentSuccessActivity

@Composable
fun PaymentResultScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Payment Status",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Button for success
        Button(
            onClick = {
                val successMessage = "Payment Successful"
                showToast(context, successMessage)
                val intent = Intent(context, PaymentSuccessActivity::class.java)
                intent.putExtra("SUCCESS_MESSAGE", successMessage)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Simulate Payment Success")
        }

        // Button for failure
        Button(
            onClick = {
                val failureMessage = "Payment Failed"
                showToast(context, failureMessage)
                val intent = Intent(context, PaymentFailedActivity::class.java)
                intent.putExtra("FAILURE_MESSAGE", failureMessage)
                context.startActivity(intent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Simulate Payment Failure")
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}