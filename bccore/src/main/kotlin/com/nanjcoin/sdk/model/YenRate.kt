package com.nanjcoin.sdk.model

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class YenRate (
    @SerializedName("USD_JPY")
    var Usd2Yen: USD_JPY
)

data class USD_JPY(
        @SerializedName("val")
        var value: BigDecimal
)

