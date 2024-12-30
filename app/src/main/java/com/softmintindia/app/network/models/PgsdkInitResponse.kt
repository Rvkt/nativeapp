package com.softmintindia.app.network.models

data class PgsdkInitResponse(
    val status: Long,
    val message: String,
    val errorMessage: Any? = null,
    val data: Data
) {
    data class Data(
        val sellerID: String,
        val companyName: String,
        val rrn: String,
        val amount: String,
        val vpa: String,
        val qrURL: String,
        val txnID: String,
        val qrService: Boolean,
        val raiseRequest: Boolean,
        val intentRequest: Boolean,
        val remark: String,
        val status: String
    )
}
