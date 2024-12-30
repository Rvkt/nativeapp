package com.softmintindia.app.domain.models

data class AuthenticatedResponse(
    val message: String,
    val user: User,
    val token: String,
    val status: Long
)

data class User(
    val userID: String,
    val roleID: Long,
    val mobile: String,
    val name: String
)

