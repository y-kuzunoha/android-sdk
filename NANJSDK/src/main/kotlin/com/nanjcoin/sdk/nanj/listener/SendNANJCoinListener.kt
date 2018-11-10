package com.nanjcoin.sdk.nanj.listener

/**
 * send NANJ coin listener
 */
interface SendNANJCoinListener {

    /**
     * on error sending
     */
    fun onError()

    /**
     * on successful sent
     */
    fun onSuccess()
}