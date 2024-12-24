package com.softmintindia.pgsdk

import android.annotation.SuppressLint
import android.app.Activity
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
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.delay


class PaymentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val companyName = intent.getStringExtra("COMPANY") ?: ""
        val amount = intent.getStringExtra("AMOUNT") ?: "0.00"
        val upiUrl = intent.getStringExtra("UPI_URL") ?: ""

        enableEdgeToEdge()
        setContent {
            AppTheme {
                Scaffold(
                    containerColor = Color(0xFF3F51B5),
                    modifier = Modifier.fillMaxSize(),
                    topBar = { AppBar() }
                ) { innerPadding ->
                    MainContent(
                        companyName = companyName,
                        amount = amount,
                        upiUrl = upiUrl,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            // You can add content inside the title if needed
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFF3F51B5), // AppBar background color
            titleContentColor = Color.White    // Title text color
        ),
        modifier = Modifier.height(24.dp) // Change the height here
    )
}






@Composable
fun MainContent(
    modifier: Modifier = Modifier,
    companyName: String,
    amount: String,
    upiUrl: String
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

        MerchantDetailsHeader(
            companyName = companyName,
            amount = "₹ $amount",
        )

        QRExpansionTile( title = "Pay using QR", iconResourceId = R.drawable.ic_qrcode, upiId = upiUrl)

        InputFieldWithSubmit(title = "Pay using UPI/VPA", iconResourceId = R.drawable.ic_qrcode)

//        Spacer(modifier = Modifier.height(24.dp))
        // todo: Add Row with app icon, app name, and right arrow
        RecommendedUPIApps()
//        Spacer(modifier = Modifier.height(16.dp))


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
                .padding(horizontal = 16.dp)
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
            .background(Color(0xFF3F51B5))
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


@SuppressLint("RememberReturnType")
@Composable
fun QRExpansionTile(title: String, iconResourceId: Int, upiId: String) {
    val expanded = remember { mutableStateOf(false) }

    // Generate the QR code bitmap
    val qrCodeBitmap = generateQRCode(upiId)

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

        // Display the dismissible alert dialog
        if (showDialog) {
            DismissibleAlertDialog(onDismiss = { showDialog = false })
        }
    }
}



@Composable
fun DismissibleAlertDialog(onDismiss: () -> Unit) {
    var timeLeft by remember { mutableStateOf(60) } // 5 minutes in seconds

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        }
        onDismiss() // Close dialog after 5 minutes
    }

    AlertDialog(
        onDismissRequest = { /* Do nothing to prevent dismissing on outside clicks */ },
        title = { Text(text = "Confirm Payment") },
        text = {
            Text(
                text = "Please make the payment using the UPI ID you entered. " +
                        "This dialog will automatically close in ${timeLeft / 60}m ${timeLeft % 60}s."
            )
        },
        confirmButton = { /* No confirm button */ },
        dismissButton = { /* No dismiss button */ },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)) // Custom shape
    )
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

    Card(modifier = Modifier
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
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = Color(0xFF3F51B5)
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(4.dp))
            }
            content(visibleButtons)
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
                        modifier = Modifier.weight(1f).padding(bottom = 16.dp)
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