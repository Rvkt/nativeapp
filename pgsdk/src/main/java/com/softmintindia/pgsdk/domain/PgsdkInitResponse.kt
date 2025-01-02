package com.softmintindia.pgsdk.domain

data class PgSdkInitResponse(
    val status: Long,
    val message: String,
    val errorMessage: Any? = null,
    val data: PgSdkInitResponseData
)

data class PgSdkInitResponseData(
    val intentRequest: Boolean,
    val qrRequest: Boolean,
    val raiseRequest: Boolean,
    val qrString: String,
    val intentString: String,
    val orderId: String,
    val amount: String,
    val remark: String,
    val identifire: String,
    val companyName: String,
    val sellerName: String
)
