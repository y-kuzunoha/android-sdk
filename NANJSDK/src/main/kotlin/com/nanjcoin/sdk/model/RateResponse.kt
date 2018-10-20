package com.nanjcoin.sdk.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

internal data class RateResponse(
        @SerializedName("statusCode")
        var statusCode: Int,
        @SerializedName("message")
        var message: String,
        @SerializedName("data")
        var data: Rate
)

internal data class Rate(
        @SerializedName("current_price")
        var currentPrice: BigDecimal
)