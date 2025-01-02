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

    fun withToken(context: Context): Map<String, String> {
        return mapOf(
//            "Authorization" to getDeviceId(context),
            "Authorization" to "U8lhxHPSNei90rUSaVebMC11fRyF1MLWrmTip+1yjInOO16/hbJVKf5f/QbRiIp69oHZ1lxqMLkq0aiwuAwtvfKWzCid83Y5zKPR4TaS3FTFgCEC+fe5vC5dTuLx6FzmYvupZRRJs1xVmTmjv8zW3alueclL8DCoesY+QOco4Eb7EmstLPdVsjmW8LDZNxDXh5ZK/ZRKSG4AKxt5wits6f9CpuEGU/VeO1mNCSTARxoF8ioaac/3jFyYWkrHz9HJ0q0D/T7tGHZhb/MnUogxS+vEN3QJtg6CaV0/Y8lEK0srfBz/EOzJDtPMi8IRoW+2VEjwsHoS3PMtjzlincbvRg==",
            "Content-Type" to "application/json"
        )
    }
}