package com.softmintindia.pgsdk.network


import com.softmintindia.pgsdk.network.models.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @POST(ApiEndpoints.AUTHENTICATE_USER)
    fun authenticateUser(
        @Header("deviceId") deviceId: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: Map<String, String>,
    ): Call<AuthenticationResponse>

    @POST(ApiEndpoints.AUTHENTICATE_USER)
    fun verifyUser(
        @Header("deviceId") deviceId: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: Map<String, String>,
    ): Call<AuthenticatedResponse>
}
