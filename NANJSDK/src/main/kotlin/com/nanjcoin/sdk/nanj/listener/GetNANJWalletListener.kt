package com.nanjcoin.sdk.nanj.listener

/**
 * this listener is for call back of Retrieving NANJ Wallet from Ethereum,
 */
interface GetNANJWalletListener {
    /**
     * on error
     */
    fun onError()

    /**
     * on successful retrieved
     */
    fun onSuccess(address: String)
}