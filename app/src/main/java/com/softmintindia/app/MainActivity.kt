package com.softmintindia.app

//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softmintindia.app.ui.theme.AppTheme
import com.softmintindia.pgsdk.PGSDKManager


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
                                        apiKey = "123456",
                                        deviceId = deviceId,
                                        companyName = "Softmint India Pvt. Ltd.",
                                        amount = "100.00", // Example amount
                                        upiUrl = "upi://pay?pa=merchant@upi&pn=Merchant&mc=1234&tid=12345&url=https://example.com",
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
            // Card displaying the app icon, label, and package name
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        /* launch the app */
                    }
                    .padding(8.dp), // Add some padding around the card
            ) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top ) {
                    // Retrieve the app icon
                    val appIcon = appInfo.loadIcon(packageManager)

                    // Convert Drawable to Bitmap and then to ImageBitmap
                    val imageBitmap = (appIcon as? BitmapDrawable)?.bitmap?.asImageBitmap()

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