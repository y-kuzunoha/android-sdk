package com.bc.core.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Transaction {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("TxHash")
    @Expose
    var txHash: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null
    @SerializedName("symbol")
    @Expose
    var symbol: String? = null
    @SerializedName("from")
    @Expose
    var from: String? = null
    @SerializedName("to")
    @Expose
    var to: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @SerializedName("tx_fee")
    @Expose
    var txFee: String? = null
    @SerializedName("time_stamp")
    @Expose
    var timeStamp: Long = 0
}
