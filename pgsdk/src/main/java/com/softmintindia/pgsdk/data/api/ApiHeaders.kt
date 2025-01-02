package com.softmintindia.pgsdk.data.api

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings

object ApiHeaders {

//    // Function to get the `deviceId`
    @SuppressLint("HardwareIds")
    private fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }
//
//    // Function to return a map of headers
//    fun deviceIdHeader(context: Context): Map<String, String> {
//        return mapOf(
//            "deviceId" to getDeviceId(context),
//            "Content-Type" to "application/json"
//        )
//    }

    fun withToken(context: Context, token: String): Map<String, String> {
        return mapOf(
//            "Authorization" to getDeviceId(context),
            "Authorization" to token,
            "Content-Type" to "application/json"
        )
    }
}