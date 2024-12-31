package com.softmintindia.pgsdk.data.api

class ApiRequests {

    data class PgsdkInitRequest(
        val amount: String,
        val remark: String,
        val identifier: String,
        val orderId: String
    )

    data class CheckTxnRequest(
        val orderId: String
    )
}
