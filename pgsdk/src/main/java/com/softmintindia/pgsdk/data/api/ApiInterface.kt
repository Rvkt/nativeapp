package com.softmintindia.pgsdk.data.api

import com.softmintindia.pgsdk.domain.CheckTxnStatusResponse
import com.softmintindia.pgsdk.domain.PgsdkInitResponse
import com.softmintindia.pgsdk.domain.UserModel
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST(ApiEndpoints.PGSDK_INITIALIZE)
    fun pgsdkInitialize(
//        @Header("deviceId") deviceId: String,
//        @Header("Content-Type") contentType: String = "application/json",
        @HeaderMap map: Map<String, String>,
        @Body requestBody: ApiRequests.PgsdkInitRequest,
    ): Call<PgsdkInitResponse>

    @GET(ApiEndpoints.CHECK_TXN_STATUS)
    fun checkTxnStatus(): Call<CheckTxnStatusResponse>
}