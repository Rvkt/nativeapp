package com.softmintindia.app.data


import com.softmintindia.app.domain.models.AuthenticatedResponse
import com.softmintindia.app.domain.models.CheckTxnStatusResponse
import com.softmintindia.app.domain.models.PgsdkInitRequest
import com.softmintindia.app.domain.models.PgsdkInitResponse
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
//        @Header("deviceId") deviceId: String,
//        @Header("Content-Type") contentType: String = "application/json",
        @HeaderMap map: Map<String, String>,
        @Body requestBody: PgsdkInitRequest,
    ): Call<PgsdkInitResponse>

    @GET(ApiEndpoints.CHECK_TXN_STATUS)
    fun checkTxnStatus(): Call<CheckTxnStatusResponse>
}
