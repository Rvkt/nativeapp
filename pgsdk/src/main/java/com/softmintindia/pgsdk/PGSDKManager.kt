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
import android.view.Window
import android.widget.ProgressBar
import android.widget.Toast
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
        callback: (Boolean, String) -> Unit
    ) {
        if (apiKey.isBlank()) {
            callback(false, "API key cannot be blank.")
            return
        }

        try {
            // Save to memory
            this.apiKey = apiKey

            // Optionally save to SharedPreferences for persistence
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(KEY_API, apiKey)
                apply()
            }


            // Show initialization dialog with progress
            showInitializationDialog(context)



            // Launch a coroutine to handle the API call
            GlobalScope.launch(Dispatchers.Main) {
                val deviceId = getDeviceId(context)
                Log.d("PGSDKManager", "Device ID: $deviceId")

                // Call the API and process the response
                callPaymentApi(context, apiKey, deviceId, amount) { success, companyName, upiUrl, qrService, raiseRequest, intentRequest, errorMessage ->
                    if (success) {

                        // todo: Log success message
                        Log.d("PGSDKManager", "Initialization successful. API Key: $apiKey, Device ID: $deviceId")


                        // If API call is successful, pass data to PaymentActivity
                        val intent = Intent(context, PaymentActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure activity starts correctly
                        intent.putExtra("COMPANY", companyName)
                        intent.putExtra("AMOUNT", amount)
                        intent.putExtra("UPI_URL", upiUrl)

                        // Pass the boolean flags for the three services
                        intent.putExtra("QR_SERVICE", qrService)
                        intent.putExtra("RAISE_REQUEST", raiseRequest)
                        intent.putExtra("INTENT_REQUEST", intentRequest)

                        context.startActivity(intent)

                        // Notify success
                        callback(true, "Initialization and API call successful.")
                    } else {
                        // If API call fails, show error and pass to ErrorActivity
                        Log.e("PGSDKManager", errorMessage)
                        val intent = Intent(context, ErrorActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure activity starts correctly
                        intent.putExtra("ERROR_MESSAGE", errorMessage)
                        context.startActivity(intent)

                        // Notify failure
                        callback(false, errorMessage)
                    }
                }
            }

        } catch (e: Exception) {
            Log.e("PGSDKManager", "Initialization error: ${e.message}", e)
            callback(false, "Initialization failed: ${e.message}")

            // Start ErrorActivity on exception
            val intent = Intent(context, ErrorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure activity starts correctly
            intent.putExtra("ERROR_MESSAGE", "Initialization failed: ${e.message}")
            context.startActivity(intent)
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
                val upiUrl = "upi://pay?pa=merchant@upi&pn=Merchant&mc=1234&tid=12345&url=https://example.com"
                val qrService = true
                val raiseRequest = true
                val intentRequest = false

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
        return apiKey ?: throw IllegalStateException("PGSDK not initialized. Call initialize() first.")
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
