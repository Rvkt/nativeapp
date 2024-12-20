package com.softmintindia.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.softmintindia.app.ui.theme.AppTheme
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.softmintindia.pgsdk.PGSDKManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi

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

                                    val deviceId = getDeviceId(context = this@MainActivity)

                                    // Initialize

                                    PGSDKManager.initialize(
                                        context = this@MainActivity,
                                        apiKey = "YOUR_MERCHANT_ID",
                                        deviceId = deviceId
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
                                modifier = Modifier.align(Alignment.CenterHorizontally).padding(16.dp) // Center the button horizontally
                            ) {
                                Text("Go to Payment")
                            }
                            InstalledAppsList() // Installed apps list will be below the button
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
    // Retrieve the list of installed packages
    val packageManager = android.content.ContextWrapper(LocalContext.current).packageManager
    val installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

    // Filter apps that can handle 'upi://' scheme
    val upiApps = installedApps.filter { appInfo ->
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("upi://")
        }
        val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        activities.any { it.activityInfo.packageName == appInfo.packageName }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(installedApps) { appInfo ->
            // Card displaying the app label and package name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), // Add some padding around the card
            ) {
                // Column inside the Card to show the app name and package name
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "App Name: ${appInfo.loadLabel(packageManager)}", style = MaterialTheme.typography.labelLarge)
                    Text(text = "Package Name: ${appInfo.packageName}", style = MaterialTheme.typography.labelSmall)
                }
            }
        }
    }
}

@SuppressLint("HardwareIds")
fun getDeviceId(context: Context): String {
    return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        InstalledAppsList()
    }
}