package com.softmintindia.pgsdk

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SdkActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve intent extras
        val token = intent.getStringExtra("token")
        val amount = intent.getStringExtra("amount")
        val remark = intent.getStringExtra("remark")
        val identifier = intent.getStringExtra("identifier")
        val orderId = intent.getStringExtra("orderId")

        setContent {
            SdkActivityScreen(
                amount = amount ?: "N/A",
                remark = remark ?: "N/A",
                onComplete = {
                    Toast.makeText(
                        this,
                        "Task completed in SDK Activity with Amount: $amount",
                        Toast.LENGTH_SHORT
                    ).show()

                    val resultIntent = intent.apply {
                        putExtra("REMARK", remark)
                        putExtra("PAYEE_NAME", "John Doe")
                        putExtra("AMOUNT", amount)
                    }
                    setResult(RESULT_OK, resultIntent)
                    finish()
                }
            )
        }
    }
}

@Composable
fun SdkActivityScreen(
    amount: String,
    remark: String,
    onComplete: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "SDK Activity",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Amount: $amount",
                fontSize = 18.sp
            )

            Text(
                text = "Remark: $remark",
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Complete SDK Task", fontSize = 16.sp)
            }
        }
    }
}

