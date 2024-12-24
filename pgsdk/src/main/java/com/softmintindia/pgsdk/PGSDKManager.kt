package com.softmintindia.pgsdk

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

object PGSDKManager {
    private const val PREF_NAME = "PGSDK_PREF"
    private const val KEY_API = "API_KEY"
    private const val KEY_DEVICE_ID = "DEVICE_ID"

    private var apiKey: String? = null
    private var deviceId: String? = null

    // Initialize function with a response callback
    fun initialize(
        context: Context,
        apiKey: String,
        deviceId: String,
        companyName: String,
        amount: String,
        upiUrl: String,
        callback: (Boolean, String) -> Unit
    ) {
        if (apiKey.isBlank() || deviceId.isBlank()) {
            callback(false, "API key and Device ID cannot be blank.")
            return
        }

        try {
            // Save to memory
            this.apiKey = apiKey
            this.deviceId = deviceId

            // Optionally save to SharedPreferences for persistence
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putString(KEY_API, apiKey)
                putString(KEY_DEVICE_ID, deviceId)
                apply()
            }

            // Log success message
            Log.d("PGSDKManager", "Initialization successful. API Key: $apiKey, Device ID: $deviceId")

            // Notify success
            callback(true, "Initialization successful.")

            // Start PaymentActivity
            val intent = Intent(context, PaymentActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Ensure activity starts correctly
            intent.putExtra("COMPANY", companyName)
            intent.putExtra("AMOUNT", amount)
            intent.putExtra("UPI_URL", upiUrl)
            context.startActivity(intent)


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

    // Retrieve API Key
    fun getApiKey(): String {
        return apiKey ?: throw IllegalStateException("PGSDK not initialized. Call initialize() first.")
    }

    // Retrieve Device ID
    fun getDeviceId(): String {
        return deviceId ?: throw IllegalStateException("PGSDK not initialized. Call initialize() first.")
    }

    // Clear data (if required)
    fun clear(context: Context) {
        apiKey = null
        deviceId = null
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}
