package com.bc.core.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Transaction {

    @SerializedName("blockNumber")
    @Expose
    var blockNumber: String? = null
    @SerializedName("timeStamp")
    @Expose
    var timeStamp: String? = null
    @SerializedName("hash")
    @Expose
    var hash: String? = null
    @SerializedName("nonce")
    @Expose
    var nonce: String? = null
    @SerializedName("blockHash")
    @Expose
    var blockHash: String? = null
    @SerializedName("from")
    @Expose
    var from: String? = null
    @SerializedName("contractAddress")
    @Expose
    var contractAddress: String? = null
    @SerializedName("to")
    @Expose
    var to: String? = null
    @SerializedName("value")
    @Expose
    var value: String? = null
    @SerializedName("tokenName")
    @Expose
    var tokenName: String? = null
    @SerializedName("tokenSymbol")
    @Expose
    var tokenSymbol: String? = null
    @SerializedName("tokenDecimal")
    @Expose
    var tokenDecimal: String? = null
    @SerializedName("transactionIndex")
    @Expose
    var transactionIndex: String? = null
    @SerializedName("gas")
    @Expose
    var gas: String? = null
    @SerializedName("gasPrice")
    @Expose
    var gasPrice: String? = null
    @SerializedName("gasUsed")
    @Expose
    var gasUsed: String? = null
    @SerializedName("cumulativeGasUsed")
    @Expose
    var cumulativeGasUsed: String? = null
    @SerializedName("input")
    @Expose
    var input: String? = null
    @SerializedName("confirmations")
    @Expose
    var confirmations: String? = null

}
