package com.nanjcoin.sdk.model

import com.google.gson.annotations.SerializedName

internal data class NANJNonce(
        @SerializedName("statusCode")
        var statusCode : Int,
        @SerializedName("data")
        var data : Int
)