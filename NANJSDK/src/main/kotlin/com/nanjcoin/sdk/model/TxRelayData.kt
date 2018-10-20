package com.nanjcoin.sdk.model

import com.google.gson.Gson
import java.math.BigInteger

internal data class TxRelayData (
        val r : String,
        val s : String,
        val v : Int,
        val data : String,
        val nonce : String,
        val dest : String,
        val hash : String
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}