package com.bc.core.nanj

interface GetNANJWalletListener {
    fun onError()
    fun onSuccess(address: String)
}