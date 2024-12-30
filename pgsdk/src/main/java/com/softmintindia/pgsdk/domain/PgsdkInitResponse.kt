package com.softmintindia.pgsdk.domain

data class PgsdkInitResponse(
    val status: Long,
    val message: String,
    val errorMessage: Any? = null,
    val data: Data
) {
    data class Data(
        val intentRequest: Boolean,
        val qrRequest: Boolean,
        val raiseRequest: Boolean,
        val qrString: String,
        val intentString: String,
        val orderID: String,
        val amount: String,
        val remark: String,
        val identifire: String,
        val companyName: String,
        val sellerName: String
    )
}
