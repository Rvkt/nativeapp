package com.softmintindia.app.domain.models

data class CheckTxnStatusResponse(
    val status: Long,
    val message: String,
    val errorMessage: Any? = null,
    val data: Data
) {
    data class Data(
        val name: String,
        val amount: String,
        val date: String,
        val time: String,
        val txnID: String,
        val remark: String,
        val status: String
    )
}
