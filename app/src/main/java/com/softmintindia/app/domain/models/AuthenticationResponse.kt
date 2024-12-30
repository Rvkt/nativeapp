package com.softmintindia.pgsdk.network.models

data class AuthenticationResponse (
    val message: String,
    val status: Long,
    val responseData: Any? = null
)

