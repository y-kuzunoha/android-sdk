package com.bc.core.nanj.listener

interface GetNANJWalletListener {
    fun onError()
    fun onSuccess(address: String)
}