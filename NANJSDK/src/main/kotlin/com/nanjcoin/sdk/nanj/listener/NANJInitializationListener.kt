package com.nanjcoin.sdk.nanj.listener

/**
 * NANJ SDK intialization listener
 */
interface NANJInitializationListener {
    /**
     * on error
     */
    fun onError()

    /**
     * on successful initialized
     */
    fun onSuccess()
}