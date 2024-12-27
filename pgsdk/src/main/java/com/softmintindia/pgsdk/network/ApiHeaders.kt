package com.softmintindia.pgsdk.network

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object ApiHeaders {

    // Function to get the `deviceId`
    @SuppressLint("HardwareIds")
    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    // Function to return a map of headers
    fun deviceIdHeader(context: Context): Map<String, String> {
        return mapOf(
            "deviceId" to getDeviceId(context),
            "Content-Type" to "application/json"
        )
    }
}