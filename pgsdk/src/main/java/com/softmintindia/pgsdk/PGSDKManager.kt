package com.softmintindia.pgsdk

/*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.internal.NoOpContinuation.context
import kotlin.coroutines.jvm.internal.CompletedContinuation.context
*/
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.provider.Settings
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.softmintindia.pgsdk.network.NetworkUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


object PGSDKManager {
    private const val PREF_NAME = "PGSDK_PREF"
    private const val KEY_API = "API_KEY"

    private var apiKey: String? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun initialize(
        context: Context,
        apiKey: String,
        amount: String,
        remark: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (apiKey.isBlank()) {
            callback(false, "API key cannot be blank.")
            return
        }

        // Save to memory
        this.apiKey = apiKey

        saveApiKeyToPreferences(context, apiKey)


//         Show initialization dialog with progress
//         showInitializationDialog(context)


        // Launch coroutine for asynchronous tasks
        launchInitializationCoroutine(context, apiKey, amount, callback)

    }


    private fun saveApiKeyToPreferences(context: Context, apiKey: String) {
        // Use lifecycleScope if calling from an Activity or Fragment
        val appContext = context.applicationContext

        // Use lifecycleScope for activity/fragment to tie the coroutine to lifecycle
        (context as? AppCompatActivity)?.lifecycleScope?.launch {
            val sharedPreferences: SharedPreferences =
                appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(KEY_API, apiKey)
                apply() // apply asynchronously
            }
        }
    }


    private fun isNetworkConnected(context: Context): Boolean {
        // Check network connection (modify as needed)
        return NetworkUtils.isConnected(context)
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun launchInitializationCoroutine(
        context: Context,
        apiKey: String,
        amount: String,
        callback: (Boolean, String) -> Unit
    ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {

                if (!isNetworkConnected(context)) {
                    callback(false, "No internet connection.")
                    return@launch
                }


                val deviceId = getDeviceId(context)
                Log.d("PGSDKManager", "Device ID: $deviceId")

                // Call the payment API and handle the response
                handlePaymentApiCall(context, apiKey, deviceId, amount, callback)

            } catch (e: Exception) {
                Log.e("PGSDKManager", "Initialization error: ${e.message}", e)
                callback(false, "Initialization failed: ${e.message}")
            }
        }
    }

    private suspend fun handlePaymentApiCall(
        context: Context,
        apiKey: String,
        deviceId: String,
        amount: String,
        callback: (Boolean, String) -> Unit
    ) {
        callPaymentApi(
            context,
            apiKey,
            deviceId,
            amount
        ) { success, companyName, upiUrl, qrService, raiseRequest, intentRequest, errorMessage ->
            if (success) {
                Log.d("PGSDKManager", "Initialization successful.")
                startPaymentActivity(
                    context, companyName, amount, upiUrl,
                    qrService, raiseRequest, intentRequest,
                )
                callback(true, "Initialization and API call successful.")
            } else {
                Log.e("PGSDKManager", errorMessage)
                showErrorActivity(context, errorMessage)
                callback(false, errorMessage)
            }
        }
    }

    // Updated callPaymentApi method to include qrService, raiseRequest, and intentRequest in the callback
    private suspend fun callPaymentApi(
        context: Context,
        apiKey: String,
        deviceId: String,
        amount: String,
        callback: (Boolean, String, String, Boolean, Boolean, Boolean, String) -> Unit
    ) {
        try {

            // Simulate a network call
            withContext(Dispatchers.IO) {
                // Simulated delay for network call
                delay(2000) // Use delay instead of Thread.sleep to avoid blocking the thread

                // Simulated API response
                val companyName = "Softmint India Pvt. Ltd."
                val upiUrl =
                    "upi://pay?pa=merchant@upi&pn=Merchant&mc=1234&tid=12345&url=https://example.com"
                val qrService = true
                val raiseRequest = true
                val intentRequest = true

                // Simulate success response
                withContext(Dispatchers.Main) {
                    // Pass the simulated data back to the callback
                    callback(true, companyName, upiUrl, qrService, raiseRequest, intentRequest, "")
                }
            }
        } catch (e: Exception) {
            // In case of error, pass error message
            withContext(Dispatchers.Main) {
                // Show Toast on the main thread
                Toast.makeText(context, "API call failed: ${e.message}", Toast.LENGTH_LONG).show()
                callback(false, "", "", false, false, false, "API call failed: ${e.message}")
            }
        }
    }

    private fun startPaymentActivity(
        context: Context,
        companyName: String,
        amount: String,
        upiUrl: String,
        qrService: Boolean,
        raiseRequest: Boolean,
        intentRequest: Boolean
    ) {
        val intent = Intent(context, PaymentActivity::class.java).apply {
            putExtra("COMPANY", companyName)
            putExtra("AMOUNT", amount)
            putExtra("UPI_URL", upiUrl)
            putExtra("QR_SERVICE", qrService)
            putExtra("RAISE_REQUEST", raiseRequest)
            putExtra("INTENT_REQUEST", intentRequest)
        }
        context.startActivity(intent)
    }

    private fun showErrorActivity(context: Context, errorMessage: String) {
        val intent = Intent(context, ErrorActivity::class.java).apply {
            putExtra("ERROR_MESSAGE", errorMessage)
        }
        context.startActivity(intent)
    }


    // Show Initialization Dialog while API call is made
    @OptIn(DelicateCoroutinesApi::class)
    private fun showInitializationDialog(context: Context) {
        // Show dialog with progress
        val dialog = Dialog(context).apply {
            setContentView(ProgressBar(context))
            setCancelable(false)
            show()
        }

        // Optional: Dismiss the dialog when done
        GlobalScope.launch {
            delay(10000) // Simulate initialization delay
            dialog.dismiss()
        }
    }


    // todo: Retrieve Device ID
    @SuppressLint("HardwareIds")
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }


    // Retrieve API Key
    fun getApiKey(): String {
        return apiKey
            ?: throw IllegalStateException("PGSDK not initialized. Call initialize() first.")
    }

    // Clear data (if required)
    fun clear(context: Context) {
        apiKey = null
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
