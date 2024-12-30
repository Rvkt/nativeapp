package com.softmintindia.app.network.models

data class PgsdkInitRequest(
    val sellerID: String,
    val amount: String,
    val remark: String,
    val token: String
)

