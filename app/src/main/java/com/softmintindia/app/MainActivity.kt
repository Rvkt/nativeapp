package com.softmintindia.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.app.ui.theme.AppTheme
import com.softmintindia.pgsdk.PGSDKManager
import com.softmintindia.pgsdk.PaymentFailedActivity
import com.softmintindia.pgsdk.PaymentSuccessActivity


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)) {
                        // Use a Column to stack the button on top
                        Column(modifier = Modifier.fillMaxSize()) {
                            Button(
                                onClick = {

                                    PGSDKManager.initialize(
                                        context = this@MainActivity,
                                        apiKey = "123456",
                                        amount = "100.00", // Example amount
                                    ) { success, message ->
                                        if (success) {
                                            // Initialization successful, PaymentActivity will start automatically
                                            Log.d("MainActivity", message)

                                            Toast.makeText(this@MainActivity, "Initialization successful", Toast.LENGTH_SHORT).show()
                                        } else {
                                            // Handle initialization failure
                                            Log.e("MainActivity", message)
                                            // Show a toast saying "Initialization failed"
                                            Toast.makeText(this@MainActivity, "Initialization failed", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                          },
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .padding(16.dp) // Center the button horizontally
                            ) {
                                Text("Go to Payment")
                            }

                            Button(
                                onClick = {
                                    val successMessage = "Payment Successful"
                                    showToast(this@MainActivity, successMessage)
                                    val intent = Intent(this@MainActivity, PaymentSuccessActivity::class.java)
                                    intent.putExtra("SUCCESS_MESSAGE", successMessage)
                                    this@MainActivity.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Text(text = "Simulate Payment Success")
                            }

                            // Button for failure
                            Button(
                                onClick = {
                                    val failureMessage = "Payment Failed"
                                    showToast(this@MainActivity, failureMessage)
                                    val intent = Intent(this@MainActivity, PaymentFailedActivity::class.java)
                                    intent.putExtra("FAILURE_MESSAGE", failureMessage)
                                    this@MainActivity.startActivity(intent)
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                            ) {
                                Text(text = "Simulate Payment Failure")
                            }
                            InstalledAppsList() // Installed apps list will be below the

                        }
                    }
                }
            }
        }
    }
}

@SuppressLint("QueryPermissionsNeeded")
@Composable
fun InstalledAppsList() {

    // Obtain the context from LocalContext
    val context = LocalContext.current

    // Retrieve the list of installed packages
    val packageManager = context.packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    // Filter apps that can handle 'upi://' scheme
    val upiApps = installedApps.filter { appInfo ->
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("upi://pay")
        }
        // Query the available activities that can handle this UPI URI
        val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        activities.any { it.activityInfo.packageName == appInfo.packageName }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(installedApps) { appInfo ->
            // Card displaying the app icon, label, and package name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        /* launch the app */
                        val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName)
                        if (intent != null) {
                            context.startActivity(intent)
                            Log.d("InstalledAppsList", "Launching app: ${appInfo.loadLabel(packageManager)} (${appInfo.packageName})")
                        } else {
                            Toast.makeText(context, "Unable to launch ${appInfo.loadLabel(packageManager)}", Toast.LENGTH_SHORT).show()
                            Log.d("InstalledAppsList", "Failed to launch app: ${appInfo.packageName}")
                        }
                    }
                    .padding(8.dp), // Add some padding around the card
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
                    // Retrieve the app icon safely
                    val appIcon = try {
                        appInfo.loadIcon(packageManager)
                    } catch (e: Exception) {
                        null // Handle cases where the icon cannot be retrieved
                    }

                    // Convert Drawable to Bitmap and then to ImageBitmap if valid
                    val imageBitmap = try {
                        val drawable = appInfo.loadIcon(packageManager)
                        if (drawable is BitmapDrawable) {
                            drawable.bitmap.asImageBitmap()
                        } else {
                            // Convert non-bitmap drawable to bitmap
                            val bitmap = Bitmap.createBitmap(
                                drawable.intrinsicWidth,
                                drawable.intrinsicHeight,
                                Bitmap.Config.ARGB_8888
                            )
                            val canvas = Canvas(bitmap)
                            drawable.setBounds(0, 0, canvas.width, canvas.height)
                            drawable.draw(canvas)
                            bitmap.asImageBitmap()
                        }
                    } catch (e: Exception) {
                        null // Handle cases where conversion fails
                    }

                    // Use BitmapPainter if the icon is a Bitmap
                    imageBitmap?.let {
                        Image(
                            painter = remember { BitmapPainter(it) },
                            contentDescription = "App Icon",
                            modifier = Modifier
                                .size(72.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    // Column inside the Row to show the app name and package name
                    Column(modifier = Modifier.align(Alignment.Top)) {
                        Text(text = "${appInfo.loadLabel(packageManager)}", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp))
                        Text(text = "Package Name: ${appInfo.packageName}", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }
    }
}

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
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
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
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        ) {
            Text(text = "Simulate Payment Failure")
        }
    }
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        InstalledAppsList()
    }
}