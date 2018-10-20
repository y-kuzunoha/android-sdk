package com.nanjcoin.sdk.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
internal class TransactionResponse : Parcelable {
    @SerializedName("status")
    var status: String? = ""
    @SerializedName("message")
    var message: String? = ""
    @SerializedName("data")
    var data: DataTransaction? = null
}

@Parcelize
class DataTransaction : Parcelable {
    @SerializedName("total")
    var total: Int = 0
    @SerializedName("limit")
    var limit: Int = 0
    @SerializedName("page")
    var page: Int = 0
    @SerializedName("max_page")
    var maxPage: Int = 0
    @SerializedName("items")
    var transactions: MutableList<NANJTransaction> = mutableListOf()
}
