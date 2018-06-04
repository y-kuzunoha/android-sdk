package com.bc.core.model

data class TxRelayData (
        val r : String,
        val s : String,
        val v : String,
        val data : String,
        val nonce : String,
        val dest : String,
        val hash : String
)