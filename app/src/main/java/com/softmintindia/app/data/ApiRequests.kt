package com.softmintindia.app.data

class ApiRequests {

    data class AuthenticationRequest(
        val userName: String,
        val password: String,
        val source: String,
        val mode: String,
        val otp: String
    )
}
