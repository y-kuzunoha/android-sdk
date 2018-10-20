package com.nanjcoin.sdk.model

import com.google.gson.annotations.SerializedName

internal data class NANJTxRelayResponse (
    @SerializedName("statusCode")
    var statusCode: Int,
    @SerializedName("message")
    var message: String? = null
)