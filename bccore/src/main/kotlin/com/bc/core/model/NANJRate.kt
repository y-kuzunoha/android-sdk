package com.bc.core.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class NANJRateData(
        @SerializedName("data")
        var data: Data
)

data class Data(
        @SerializedName("quotes")
        var quotes: Quotes
)

data class Quotes(
        @SerializedName("USD")
        var USD: USD,
        @SerializedName("NANJ")
        var NANJ: NANJ
)

data class USD(
        @SerializedName("price")
        var price: BigDecimal
)

data class NANJ(
        @SerializedName("price")
        var price: BigDecimal
)