package com.bc.core.model

import com.google.gson.annotations.SerializedName

class TransactionResponse {
    @SerializedName("status")
    var status: String? = ""
    @SerializedName("message")
    var message: String? = ""
    @SerializedName("result")
    var transactions: MutableList<Transaction> = mutableListOf()
}
