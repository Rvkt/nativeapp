package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.softmintindia.pgsdk.ui.theme.AppTheme


class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
//                    topBar = { AppBar() }
                ) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}



//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppBar() {
//    TopAppBar(
//        title = {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                // White square on the left side of the title
//                Box(
//                    modifier = Modifier
//                        .size(48.dp) // Adjust the size of the square
//                        .background(Color.White) // Set the color to white
//                )
//                Spacer(modifier = Modifier.width(8.dp)) // Add some space between image and title text
//                Text(
//                    text = "PGSDK - Payment Options",
//                    fontSize = 18.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Color.White,
//                    textAlign = TextAlign.Center
//                )
//            }
//        },
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = Color(0xFF3F51B5), // AppBar background color
//            titleContentColor = Color.White    // Title text color
//        )
//    )
//}





@Composable
fun MainContent(modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(Color(0xFFF6F7FF))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        MerchantDetailsHeader(
            companyName = "Softmint India",
            amount = "₹ 5000",
        )

        QRExpansionTile( title = "Pay using QR", iconResourceId = R.drawable.ic_qrcode, upiId = "https://example.com")

        Spacer(modifier = Modifier.height(24.dp))
//        AmountDisplay()
//        Spacer(modifier = Modifier.height(16.dp))
        // todo: QR image.
//        PaymentScreen(content = "https://example.com")
        // todo: Add Row with app icon, app name, and right arrow
        RecommendedUPIApps()
        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(16.dp))

        ExpansionTile(
            title = "All Payment Options",
            content = { visibleButtons ->
                ButtonList(visibleButtons = visibleButtons)
            }
        )



        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { Log.d("ButtonClick", "Continue clicked") },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF3F51B5)  ),
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Continue",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun MerchantDetailsHeader(companyName: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Color(0xFF3F51B5)) // Set a custom background color here
            .padding(16.dp), // Optional padding
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start, // Align items to the start
    ) {
        // White square on the left side of the title
        Box(
            modifier = Modifier
                .size(96.dp)
                .padding(0.dp)
//                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
//                .border(
//                    width = 1.dp,
//                    color = Color(0xFFFFFFFF),
//                    shape = RoundedCornerShape(8.dp)
//                )
        )

        Spacer(modifier = Modifier.width(24.dp))

        // Column to hold the company name and amount
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = companyName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = amount,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Start
            )
        }
    }
}



@Composable
fun RecommendedUpiAppCard(
    appName: String,
    iconResourceId: Int,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 0.dp, // Border width
                color = Color(0xFFE9E9E9), // Border color
                shape = RoundedCornerShape(8.dp) // Rounded corners for the border
            )
            .padding(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 0.dp), // Add some elevation for the card
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Button(
            onClick = onClick,
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // App Icon
                Image(
                    painter = painterResource(id = iconResourceId),
                    contentDescription = "$appName Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))

                // App Name
                Text(
                    text = appName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C6BC0),
                    modifier = Modifier.weight(1f)
                )

                // Right Arrow Icon
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = Color(0xFFDFDFDF)
                )
            }
        }
    }
}

@Composable
fun RecommendedUPIApps() {
    val context = LocalContext.current
    val paymentHelper = PaymentHelper()

    val upiPaymentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->

            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
//                val upiResult = data?.getStringExtra("response") ?: ""
                paymentHelper.handleUpiPaymentResponse(context, data)
            } else {
                Toast.makeText(context, "Transaction failed or canceled.", Toast.LENGTH_SHORT).show()
            }
        }
    )



    Column {
        Text(
            text = "Recommended",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp, // Border width
                    color = Color(0xFFE9E9E9), // Border color
                    shape = RoundedCornerShape(8.dp) // Rounded corners for the border
                )
                .padding(0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 0.dp),
            shape = RoundedCornerShape(8.dp), // Rounded corners for the card
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE9E9E9)
            )
            RecommendedUpiAppCard(
                appName = "Phone Pe",
                iconResourceId = R.drawable.ic_phonepe
            ) {

                paymentHelper.initiateUpiPayment(
                    context = context,
                    amount = "1",
                    upiId = "8860733786@ybl",
                    name = "Ravi Kant",
                    txnId = "TXN20241219160431",
                    url = "EASYSWIFT SERVICES PVT LTD",
                    note = "PAYMENT",
                    upiAppPackage = "com.phonepe.app",
                    upiPaymentLauncher = upiPaymentLauncher
                )

//                launchPhonePeApp(context)
                Log.d("UPI APP", "App Name: Phone Pe")
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = Color(0xFFE9E9E9)
            )
            RecommendedUpiAppCard(
                appName = "Google Pay",
                iconResourceId = R.drawable.ic_googlepay
            ) {

                paymentHelper.initiateUpiPayment(
                    context = context,
                    amount = "1",
                    upiId = "8860733786@ybl",
                    name = "Ravi Kant",
                    txnId = "TXN20241219160431",
                    url = "EASYSWIFT SERVICES PVT LTD",
                    note = "PAYMENT",
                    upiAppPackage = "com.google.android.apps.nbu.paisa.user",
                    upiPaymentLauncher = upiPaymentLauncher
                )
                Log.d("UPI APP", "App Name: Google Pay")
            }

        }
    }
}

@Composable
fun AmountDisplay() {
    Column(modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Amount",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "₹1000",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
fun PaymentScreen(content: String) {
    // Remember the state for showing the QR code dialog
    val showQrCodeDialog = remember { mutableStateOf(false) }

    // Generate the QR code bitmap
    val qrCodeBitmap = generateQRCode(content)

    Box(modifier = Modifier.wrapContentSize()) {
        // Button to toggle QR code dialog visibility
        Button(
            onClick = { showQrCodeDialog.value = true },
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text("Pay using QR")
        }

        // Show the QR code dialog when the button is clicked
        if (showQrCodeDialog.value) {
            AlertDialog(
                onDismissRequest = { showQrCodeDialog.value = false },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("QR Code")
                    }
                },
                text = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Show QR Code or error message inside the Row
                        if (qrCodeBitmap != null) {
                            Image(
                                bitmap = qrCodeBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier.size(200.dp)  // Adjust the size of the QR code
                            )
                        } else {
                            Text("Failed to generate QR code")
                        }
                    }
                },
                confirmButton = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { showQrCodeDialog.value = false }
                        ) {
                            Text("Close")
                        }
                    }
                }
            )
        }
    }
}


private fun generateQRCode(content: String): Bitmap? {
    val writer = MultiFormatWriter()
    return try {
        val bitMatrix: BitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, 512, 512)
        val barcodeEncoder = BarcodeEncoder()
        barcodeEncoder.createBitmap(bitMatrix)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}





fun launchPhonePeApp(context: Context) {
    try {
        // Attempt to launch the PhonePe app using its package name
        val intent = context.packageManager.getLaunchIntentForPackage("com.phonepe.app")
        if (intent != null) {
            context.startActivity(intent)  // Launch the app
        } else {
            // If PhonePe is not installed, redirect to Play Store
            val playStoreIntent = Intent(Intent.ACTION_VIEW,
                android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.phonepe.app"))
            context.startActivity(playStoreIntent)
        }
    } catch (e: PackageManager.NameNotFoundException) {
        // Handle case where the package is not found
        Toast.makeText(context, "PhonePe app is not installed", Toast.LENGTH_SHORT).show()
    }
}

@SuppressLint("RememberReturnType")
@Composable
fun QRExpansionTile(title: String, iconResourceId: Int, upiId: String) {
    val expanded = remember { mutableStateOf(false) }

    // Generate the QR code bitmap
    val qrCodeBitmap = generateQRCode(upiId)

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp)) {
        Text(
            text = "Preferred Payment Methods",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSystemInDarkTheme()) Color.White else Color.Gray,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(top = 24.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.dp,
                    color = Color(0xFFE9E9E9),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(0.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 0.dp),
            shape = RoundedCornerShape(8.dp), // Rounded corners for the card
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )

        ) {
            Column(
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Header with icon to toggle expansion
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable (
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { expanded.value = !expanded.value }
                        .padding(start = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = iconResourceId),
                        contentDescription = "$title Icon",
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C6BC0),
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    IconButton(onClick = { expanded.value = !expanded.value }) {
                        Icon(
                            imageVector = if (expanded.value) {
                                Icons.Filled.KeyboardArrowUp
                            } else {
                                Icons.Filled.KeyboardArrowDown
                            },
                            tint = if (expanded.value) Color.Gray else LocalContentColor.current,
                            contentDescription = "Expand/Collapse"
                        )
                    }
                }

                // Content that expands or collapses
                AnimatedVisibility(visible = expanded.value) {
                    Column(
                        modifier = Modifier
                            .padding(top = 0.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (qrCodeBitmap != null) {
                            Image(
                                bitmap = qrCodeBitmap.asImageBitmap(),
                                contentDescription = "QR Code",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f) // Maintain aspect ratio
                            )
                        } else {
                            Text("Failed to generate QR code")
                        }
                    }
                }
            }
        }
    }
}







// TODO: expansion tile for the all payment options
@Composable
fun ExpansionTile(title: String, content: @Composable (visibleButtons: List<String>) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }

    val buttonList = listOf(
        "Google Pay", "Phone Pe", "Paytm", "Amazon Pay",
        "MobiKwik", "FreeCharge", "WhatsApp Pay", "Bhim UPI",
        "PhonePe UPI", "Google UPI",
        "Apple Pay", "Samsung Pay", "PayPal", "Razorpay",
        "JioMoney", "AirTel Payments", "HDFC Pay", "ICICI Pay",
        "Kotak Pay", "SBI Pay", "Axis Pay", "Baroda Pay",
        "Yono SBI", "Bajaj Pay", "Vodafone M-Pesa", "Ola Money",
        "Zeta Pay", "Simpl Pay", "Ipay", "Cashfree Pay", "Pine Labs"
    )


    // Determine which buttons to show based on expansion state
    val visibleButtons = if (isExpanded) buttonList else buttonList.take(4)

    Card(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight(0.775f)
            .border(
                width = 1.dp, // Border width
                color = Color(0xFFE9E9E9), // Border color
                shape = RoundedCornerShape(8.dp) // Rounded corners for the border
            )
            .padding(0.dp), // Card padding
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 0.dp), // Optional elevation for shadow effect
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8EAF6)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp) // Padding for content inside the Card
        ) {
            // Header of the ExpansionTile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .clickable { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5),
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF3F51B5)
                )
            }

//            AnimatedVisibility(
//                visible = isExpanded,
//                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
//                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
//            ) {
//                Column(modifier = Modifier.animateContentSize()) {
//                    Spacer(modifier = Modifier.height(8.dp)) // Spacer for a little spacing before the content
//                    content(visibleButtons) // Pass the visible buttons list to the content
//                }
//            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            content(visibleButtons)
        }
    }
}


@Composable
fun ButtonList(visibleButtons: List<String>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // 2 buttons per row
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(4.dp)
    ) {
        items(visibleButtons) { button ->
            Button(
                onClick = { Log.d("ButtonClick", "App Name: $button") },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp) // Padding between buttons
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Replace with actual icons for each button
                    Image(
                        painter = painterResource(id = R.drawable.ic_googlepay), // Replace with dynamic icons
                        contentDescription = "App Icon",
                        modifier = Modifier.size(8.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    // App Name
                    Text(
                        text = button,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5C6BC0),
                        modifier = Modifier.weight(1f)
                    )


                    // Right Arrow Icon
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFFDFDFDF)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainContentPreview() {
    AppTheme {
        Scaffold(
//            topBar = { AppBar() }
        ) {
            MainContent(Modifier.padding(it))
        }
    }
}