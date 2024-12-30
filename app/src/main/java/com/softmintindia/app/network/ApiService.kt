package com.softmintindia.app.network


import com.softmintindia.app.network.models.AuthenticatedResponse
import com.softmintindia.app.network.models.CheckTxnStatusResponse
import com.softmintindia.app.network.models.PgsdkInitRequest
import com.softmintindia.app.network.models.PgsdkInitResponse
import com.softmintindia.pgsdk.network.ApiEndpoints
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

    @POST(ApiEndpoints.PGSDK_INITIALIZE)
    fun pgsdkInitialize(
        @Header("deviceId") deviceId: String,
        @Header("Content-Type") contentType: String = "application/json",
        @Body requestBody: PgsdkInitRequest,
    ): Call<PgsdkInitResponse>

    @GET(ApiEndpoints.CHECK_TXN_STATUS)
    fun checkTxnStatus(): Call<CheckTxnStatusResponse>
}
