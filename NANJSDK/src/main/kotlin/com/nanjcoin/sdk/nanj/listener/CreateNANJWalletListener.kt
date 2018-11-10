package com.nanjcoin.sdk.nanj.listener

/**
 * internal create wallet listener
 */
internal interface CreateNANJWalletListener {
    /**
     * on error
     */
    fun onError()

    /**
     * on success
     */
    fun onSuccess()
}