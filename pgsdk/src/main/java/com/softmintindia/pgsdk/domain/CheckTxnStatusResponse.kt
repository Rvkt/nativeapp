package com.softmintindia.pgsdk.domain

data class CheckTxnStatusResponse(
    val status: Long,
    val message: String,
    val errorMessage: Any? = null,
    val data: CheckTxnResponseData
)

data class CheckTxnResponseData(
    val name: String,
    val amount: String,
    val date: String,
    val time: String,
    val orderId: String,
    val remark: String,
    val status: String,
    val rrn: String,
    val responseCode: String,
    val payeeVpa: String,
    val identifire: String
)




