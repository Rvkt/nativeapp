package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.softmintindia.pgsdk.ui.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.*


class PaymentActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve the intent extras
        val companyName = intent.getStringExtra("COMPANY") ?: ""
        val amount = intent.getStringExtra("AMOUNT") ?: "0.00"
        val upiUrl = intent.getStringExtra("UPI_URL") ?: ""

        // Retrieve the service flags
        val qrService = intent.getBooleanExtra("QR_SERVICE", false)
        val raiseRequest = intent.getBooleanExtra("RAISE_REQUEST", false)
        val intentRequest = intent.getBooleanExtra("INTENT_REQUEST", false)

//        Log.d(
//            "PaymentActivity",
//            "Company: $companyName, Amount: $amount, UPI URL: $upiUrl, QR Service: $qrService, Raise Request: $raiseRequest, Intent Request: $intentRequest"
//        )


        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    containerColor = Color(0xFF3F51B5),
                    modifier = Modifier.fillMaxSize(),
//                    topBar = { AppBar(companyName, amount) }
                    topBar = { LargeTopAppBarExample() }
                )
                { innerPadding ->
                    MainContent(
                        companyName = companyName,
                        amount = amount,
                        upiUrl = upiUrl,
                        qrService = qrService,
                        raiseRequest = raiseRequest,
                        intentRequest = intentRequest,
                        activity = this,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



//@RequiresApi(Build.VERSION_CODES.O)
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AppBar(companyName: String, amount: String) {
//    TopAppBar(
//        windowInsets = TopAppBarDefaults.windowInsets,
////        scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
//        title = {
//            val currentDateTime = LocalDateTime.now()
//            val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
//            val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
//
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(180.dp)
//                    .background(Color(0xFF3F51B5))
//                    .padding(top = 16.dp), // Optional padding
//                verticalAlignment = Alignment.Top,
//                horizontalArrangement = Arrangement.Start, // Align items to the start
//            ) {
//                // White square on the left side of the title
//                Box(
//                    modifier = Modifier
//                        .size(96.dp)
//                        .padding(0.dp)
//                        .clip(CircleShape)
//                        .background(Color.White)
//
//                ) {
//                    Image(
//                        painter = painterResource(id = R.drawable.ic_default), // Replace with your image resource ID
//                        contentDescription = "Your image description", // Provide an appropriate description
//                        modifier = Modifier
//                            .padding(16.dp)
//                            .align(Alignment.Center) // Align the image in the center of the Box
//                            .wrapContentSize() // The image will take only the space it needs
//                    )
//                }
//
//
//
//                Spacer(modifier = Modifier.width(24.dp))
//
//                // Column to hold the company name and amount
//                Column(
//                    verticalArrangement = Arrangement.Top,
//                    horizontalAlignment = Alignment.Start
//                ) {
//                    Text(
//                        text = companyName,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White,
//                        fontFamily = FontFamily.Serif,
//                        textAlign = TextAlign.Start
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = amount,
//                        fontSize = 24.sp,
//                        fontFamily = FontFamily.SansSerif,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White,
//                        textAlign = TextAlign.Start
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text(
//                        text = "${currentDateTime.format(timeFormatter)}  ${currentDateTime.format(dateFormatter)}",
//                        fontSize = 16.sp,
//                        fontFamily = FontFamily.SansSerif,
//                        fontWeight = FontWeight.Bold,
//                        color = Color.White,
//                        textAlign = TextAlign.End
//                    )
//
//                }
//            }
//        },
//        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
//            containerColor = Color(0xFF3F51B5), // AppBar background color
//            titleContentColor = Color.White    // Title text color
//        ),
//        modifier = Modifier.height(200.dp) // Change the height here
//    )
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LargeTopAppBarExample() {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3F51B5),
            titleContentColor = Color.White,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                // Image on the left
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .padding(0.dp)
                        .wrapContentSize()
                        .clip(RoundedCornerShape(4.dp)) // Round the corners by 4 dp
                        .background(Color.White)
                        .border(0.dp, Color(0xFFADD8E6), RoundedCornerShape(4.dp)) // Add light blue border with rounded corners
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.ic_default), // Replace with your image resource ID
                        contentDescription = "Your image description", // Provide an appropriate description
                        modifier = Modifier
                            .padding(4.dp)
                            .align(Alignment.Center) // Align the image in the center of the Box
                            .wrapContentSize() // The image will take only the space it needs
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))


                Column (
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start
                ){
//                    Text(
//                        text = "Large Top App Bar",
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis,
//                        style = MaterialTheme.typography.titleMedium
//                    )

                    Text(
                        text = "Softmint India Pvt. Ltd.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Start
                    )

                    Spacer(modifier = Modifier.height(4.dp)) // Add spacing between the title and amount
                    // Amount Text
                    Text(
                        text = "₹ 123",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
                        color = Color.White
                    )
                }

            }
        },
        scrollBehavior = scrollBehavior
    )
}







@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    companyName: String,
    amount: String,
    upiUrl: String,
    qrService: Boolean,
    raiseRequest: Boolean,
    intentRequest: Boolean,
    activity: ComponentActivity
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .fillMaxHeight()
            .background(Color(0xFFF6F7FF))
            .verticalScroll(rememberScrollState())
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        if (qrService) {
            QRExpansionTile(
                title = "Pay using QR",
                iconResourceId = R.drawable.ic_qrcode,
                showExpanded = remember { mutableStateOf( !raiseRequest && !intentRequest) },
                upiId = upiUrl,
                activity = activity
            )
        }

        if (raiseRequest) {
            InputFieldWithSubmit(
                title = "Pay using UPI/VPA",
                iconResourceId = R.drawable.ic_qrcode
            )
        }


        // todo: Add Row with app icon, app name, and right arrow
        if (intentRequest) {
            RecommendedUPIApps()
            InstalledUpiAppsExpansionTile(
                title = "Installed Payment Options",
//                content = { visibleButtons ->
//                    ButtonList(visibleButtons = visibleButtons)
//                }
            )
        }

//        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { Log.d("ButtonClick", "Continue clicked") },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF3F51B5)  ),
            modifier = Modifier
//                .height(64.dp)
                .padding(16.dp)
                .wrapContentHeight()
                .fillMaxWidth()
        ) {
            Text(
                modifier= Modifier.padding(8.dp),
                text = "Continue",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Powered by UPI
//        Spacer(modifier = Modifier.height(32.dp))
        Spacer(modifier = Modifier.weight(1f))





        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 48.dp, vertical = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = "POWERED BY",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_upi),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .heightIn(max = 24.dp)  // Limit the height of the image
                        .padding(top = 4.dp)     // Padding between text and image
                )
            }

            Spacer(modifier = Modifier.weight(1f))  // Add space between the two columns

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.wrapContentWidth()
            ) {
                Text(
                    text = "TRANSACTION PARTNER",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    textAlign = TextAlign.Center,
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_softmint),
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .heightIn(max = 24.dp)  // Limit the height of the image
                        .padding(top = 4.dp)     // Padding between text and image
                )
            }
        }

    }
}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun MerchantDetailsHeader(companyName: String, amount: String) {
//    val currentDateTime = LocalDateTime.now()
//    val dateFormatter = DateTimeFormatter.ofPattern("d MMM, yyyy")
//    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")
//
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .wrapContentHeight()
//            .background(Color(0xFF3F51B5))
//            .padding(16.dp), // Optional padding
//        verticalAlignment = Alignment.Top,
//        horizontalArrangement = Arrangement.Start, // Align items to the start
//    ) {
//        // White square on the left side of the title
//        Box(
//            modifier = Modifier
//                .size(96.dp)
//                .padding(0.dp)
//                .clip(CircleShape)
//                .background(Color.White)
//
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_default), // Replace with your image resource ID
//                contentDescription = "Your image description", // Provide an appropriate description
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(Alignment.Center) // Align the image in the center of the Box
//                    .wrapContentSize() // The image will take only the space it needs
//            )
//        }
//
//
//
//        Spacer(modifier = Modifier.width(24.dp))
//
//        // Column to hold the company name and amount
//        Column(
//            verticalArrangement = Arrangement.Top,
//            horizontalAlignment = Alignment.Start
//        ) {
//            Text(
//                text = companyName,
//                fontSize = 20.sp,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                fontFamily = FontFamily.Serif,
//                textAlign = TextAlign.Start
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = amount,
//                fontSize = 24.sp,
//                fontFamily = FontFamily.SansSerif,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.Start
//            )
//            Spacer(modifier = Modifier.height(8.dp))
//            Text(
//                text = "${currentDateTime.format(timeFormatter)}  ${
//                    currentDateTime.format(
//                        dateFormatter
//                    )
//                }",
//                fontSize = 16.sp,
//                fontFamily = FontFamily.SansSerif,
//                fontWeight = FontWeight.Bold,
//                color = Color.White,
//                textAlign = TextAlign.End
//            )
//
//        }
//    }
//}



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



    Column (modifier = Modifier.padding(16.dp) ){

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
                .padding(horizontal = 0.dp),
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


@SuppressLint("RememberReturnType", "DefaultLocale")
@Composable
fun QRExpansionTile(title: String, iconResourceId: Int, upiId: String, showExpanded: MutableState<Boolean>, activity: ComponentActivity) {



    // Generate the QR code bitmap
    val qrCodeBitmap = generateQRCode(upiId)

    // State for the timer countdown
    var timeLeft by remember { mutableStateOf(1 * 60 * 1000L) } // 5 minutes in milliseconds
    var formattedTime by remember { mutableStateOf("01:00") }

    // Timer countdown logic using LaunchedEffect
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            launch {
                kotlinx.coroutines.delay(1000L)
                timeLeft -= 1000L
                formattedTime = String.format("%02d:%02d", timeLeft / 60000, (timeLeft % 60000) / 1000)
            }
        } else {
            // Close the activity when time reaches zero
//            activity.finish()
        }
    }


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)) {
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
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { showExpanded.value = !showExpanded.value }
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
                    IconButton(onClick = { showExpanded.value = !showExpanded.value }) {
                        Icon(
                            imageVector = if (showExpanded.value) {
                                Icons.Filled.KeyboardArrowUp
                            } else {
                                Icons.Filled.KeyboardArrowDown
                            },
                            tint = if (showExpanded.value) Color.Gray else LocalContentColor.current,
                            contentDescription = "Expand/Collapse"
                        )
                    }
                }

                // Content that expands or collapses
                AnimatedVisibility(visible = showExpanded.value) {
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

                // Timer Text below the QR code
                Text(
                    text = "Time remaining: $formattedTime",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Composable
fun InputFieldWithSubmit(title: String, iconResourceId: Int) {

    val expanded = remember { mutableStateOf(false) }

    // State for holding the text input
    var inputText by remember { mutableStateOf("") }



    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)){
        Text(
            text = "Raise UPI Request",
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
                        .clickable(
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

                    UPIValidationForm()

                }
            }
        }
    }


}

@Composable
fun UPIValidationForm() {
    var upiId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var isUpiValid by remember { mutableStateOf(false) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }

    val verifyButtonFocusRequester = remember { FocusRequester() }
    val submitButtonFocusRequester = remember { FocusRequester() }

    val focusManager = LocalFocusManager.current

    // Function to validate UPI ID
    fun validateUpiId(upi: String): Boolean {
        val regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$".toRegex()
        return regex.matches(upi)
    }

    LaunchedEffect(isUpiValid) {
        if (isUpiValid) {
            submitButtonFocusRequester.requestFocus()
        } else {
            verifyButtonFocusRequester.requestFocus()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // UPI ID Input Field
        OutlinedTextField(
            value = upiId,
            maxLines = 1,
            onValueChange = { upiId = it },
            label = { Text("Enter UPI ID") },
            isError = !isUpiValid && upiId.isNotEmpty(),
            keyboardActions = KeyboardActions(
                onDone = {
                    // Validate the UPI ID on 'Done'
                    isUpiValid = validateUpiId(upiId)
                    if (isUpiValid) {
                        focusManager.clearFocus() // Clear focus from the input field
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Conditionally display the name if UPI ID is valid
        if (isSubmitted && name.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Name: $name",
                fontSize = 16.sp,
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Conditionally show either "VERIFY UPI" or "SUBMIT" button
        if (!isUpiValid) {
            // Show "VERIFY UPI" button if the UPI ID is not yet valid
            Button(
                onClick = {
                    isUpiValid = validateUpiId(upiId)
                    if (isUpiValid) {
                        name = "John Doe" // Replace with dynamic logic if necessary
                        isSubmitted = true
                        focusManager.clearFocus() // Clear focus from the input field
                    } else {
                        name = ""
                        isSubmitted = false
                    }
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF3F51B5)),
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .focusRequester(verifyButtonFocusRequester)
            ) {
                Text(
                    text = "VERIFY UPI",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Show "SUBMIT" button if the UPI ID is valid
            Button(
                onClick = {
                    showDialog = true // Show the confirmation dialog
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF3F51B5)),
                modifier = Modifier
                    .height(64.dp)
                    .fillMaxWidth()
                    .focusRequester(submitButtonFocusRequester)
            ) {
                Text(
                    text = "SUBMIT",
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(
            onClick = {
                showDialog = true // Show the confirmation dialog
            },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color(0xFF3F51B5)),
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
                .focusRequester(submitButtonFocusRequester)
        ) {
            Text(
                text = "TEST ALERT DIALOG",
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Display the dismissible alert dialog
        if (showDialog) {
//            DismissibleAlertDialog(onDismiss = { showDialog = false })
            DismissibleAlertDialog(
                onDismiss = { showDialog = false },
                payingTo = "John Doe",
                amount = "500",
                companyName = "Clients Company's Name",
                imageResource = R.drawable.ic_paytm
            )

        }
    }
}


@SuppressLint("DefaultLocale")
@Composable
fun DismissibleAlertDialog(
    onDismiss: () -> Unit,
    companyName: String,
    payingTo: String,
    amount: String,
    imageResource: Int
) {
    var timeLeft by remember { mutableIntStateOf(60) } // 1 minute in seconds

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        }
        onDismiss() // Close dialog after 1 minute
    }

    AlertDialog(
        containerColor = Color.White,
        onDismissRequest = { /* Do nothing to prevent dismissing on outside clicks */ },
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                // Displaying the image at the top
                Image(
                    painter = painterResource(id = R.drawable.ic_softmint),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                )

                Text(
                    text = companyName,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp),
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Row containing "Paying to" and "Amount"
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Paying to",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = payingTo,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .wrapContentHeight(),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Amount",
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = amount,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Horizontal Divider
                HorizontalDivider(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .fillMaxWidth(),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .heightIn(min = 300.dp, max = 400.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = buildAnnotatedString {
                        append("Please authorize payment of ")
                        withStyle(style = SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold)) {
                            append(amount) // Color the amount text
                        }
                        append(" on your UPI mobile app within:")
                    },
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 16.sp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Column for Minutes
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .weight(1f) // Distribute space evenly
                    ) {
                        Text(
                            text = String.format("%02d", timeLeft / 60), // Remaining minutes
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Minutes",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    // Colon separator
                    Text(
                        text = ":",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    // Column for Seconds
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f) // Distribute space evenly
                    ) {
                        Text(
                            text = String.format("%02d", timeLeft % 60), // Remaining seconds
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Seconds",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // Powered by UPI
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "POWERED BY",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                    textAlign = TextAlign.Center
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_upi),
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)
                        .wrapContentHeight()
                        .fillMaxWidth()
                )
            }
        },
        confirmButton = { /* No confirm button */ },
        dismissButton = { /* No dismiss button */ },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)) // Custom shape
    )

}











// TODO: expansion tile for the all payment options
@SuppressLint("QueryPermissionsNeeded")
@Composable
fun InstalledUpiAppsExpansionTile(
    title: String,
    context: android.content.Context = LocalContext.current // Obtain the context from LocalContext
) {
    var isExpanded by remember { mutableStateOf(false) }
    val packageManager = context.packageManager

    // Get the list of installed UPI apps
    val installedUpiApps = remember {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("upi://pay")
        }

        // Retrieve apps that can handle the UPI intent
        val activities = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)

        activities.mapNotNull { resolveInfo ->
            val appInfo = resolveInfo.activityInfo?.applicationInfo
            appInfo?.let {
                Triple(
                    appInfo.loadLabel(packageManager).toString(),
                    appInfo.loadIcon(packageManager), // Get the app's icon
                    resolveInfo.activityInfo.packageName // Add the app's package name for launching
                )
            }
        }.sortedBy { it.first } // Sort by app name
    }

    // Determine which apps to show based on the expansion state
    val visibleApps = if (isExpanded) installedUpiApps else installedUpiApps.take(4)

    // Card with expansion tile UI
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .border(
                width = 0.dp,
                color = Color(0xFFE9E9E9),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(0.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp, focusedElevation = 0.dp),
        shape = RoundedCornerShape(8.dp), // Rounded corners for the card
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8EAF6)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Header of the ExpansionTile
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { isExpanded = !isExpanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3F51B5),
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF3F51B5)
                )
            }

            if (visibleApps.isEmpty()) {
                Text(
                    text = "No UPI apps installed.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            } else {
                // Display installed UPI apps in rows (4 per row)
                Column {
                    visibleApps.chunked(4).forEach { rowButtons ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between buttons
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowButtons.forEach { (appName, appIcon, packageName) ->
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
//                                        .weight(1f)
                                        .padding(bottom = 16.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            // Launch the app when tapped
                                            try {
                                                val intent = packageManager.getLaunchIntentForPackage(packageName)
                                                intent?.let {
                                                    context.startActivity(it)
                                                }
                                            } catch (e: Exception) {
                                                // Handle any errors (app not found, etc.)
                                                Toast.makeText(context, "Unable to open $appName", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(64.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                                        modifier = Modifier
                                            .padding(vertical = 0.dp)
                                    ) {
                                        // Display the actual app icon
                                        val imageBitmap = try {
                                            if (appIcon is BitmapDrawable) {
                                                appIcon.bitmap.asImageBitmap()
                                            } else {
                                                // Convert non-bitmap drawable to bitmap
                                                val bitmap = Bitmap.createBitmap(
                                                    appIcon.intrinsicWidth,
                                                    appIcon.intrinsicHeight,
                                                    Bitmap.Config.ARGB_8888
                                                )
                                                val canvas = Canvas(bitmap)
                                                appIcon.setBounds(0, 0, canvas.width, canvas.height)
                                                appIcon.draw(canvas)
                                                bitmap.asImageBitmap()
                                            }
                                        } catch (e: Exception) {
                                            null // Handle cases where conversion fails
                                        }

                                        imageBitmap?.let {
                                            Image(
                                                painter = BitmapPainter(it),
                                                contentDescription = "$appName Icon",
                                                modifier = Modifier.size(40.dp)
                                            )
                                        }
                                    }

                                    // Display app name
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = appName,
                                        fontSize = 12.sp,
                                        maxLines = 2,
                                        textAlign = TextAlign.Center,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black
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





@Composable
fun ButtonList(visibleButtons: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Group buttons into rows
        visibleButtons.chunked(4).forEach { rowButtons ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp), // Space between buttons
                verticalAlignment = Alignment.CenterVertically
            ) {
                rowButtons.forEach { button ->
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .weight(1f)
                            .padding(bottom = 16.dp)
                    ){

                        Button(
                            onClick = { Log.d("ButtonClick", "App Name: $button") },
                            shape = RoundedCornerShape(64.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_googlepay), // Replace with dynamic icons
                                contentDescription = "App Icon",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                        Text(
                            text = button,
                            fontSize = 12.sp,
                            maxLines = 2,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            color = Color(0xFF5C6BC0),
//                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//fun MainContentPreview() {
//    AppTheme {
//        Scaffold(
//            topBar = { AppBar() }
//        ) {
//            MainContent(
//                amount = "₹ 100.00",  // Example static amount
//                upiUrl = "upi://pay?pa=example@upi&pn=MerchantName&mc=1234&tid=12345&tr=1234567890&tn=Payment&am=100&cu=INR",
//                modifier = Modifier.padding(it)
//            )
//        }
//    }
//}