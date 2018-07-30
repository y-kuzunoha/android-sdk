package com.nanjcoin.sdk.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NANJTransaction(
        @SerializedName("id")
        var id: Int? = null,
        @SerializedName("TxHash")
        var txHash: String? = null,
        @SerializedName("status")
        var status: Int? = null,
        @SerializedName("created_at")
        var createdAt: String? = null,
        @SerializedName("symbol")
        var symbol: String? = null,
        @SerializedName("from")
        var from: String? = null,
        @SerializedName("to")
        var to: String? = null,
        @SerializedName("value")
        var value: String? = null,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("tx_fee")
        var txFee: String? = "",
        @SerializedName("time_stamp")
        var timeStamp: Long = 0
) : Parcelable


