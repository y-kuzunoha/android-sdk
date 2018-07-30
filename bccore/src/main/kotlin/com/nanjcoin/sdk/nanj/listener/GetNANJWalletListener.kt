package com.nanjcoin.sdk.nanj.listener

interface GetNANJWalletListener {
    fun onError()
    fun onSuccess(address: String)
}