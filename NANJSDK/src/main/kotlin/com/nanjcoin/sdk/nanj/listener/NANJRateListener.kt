package com.nanjcoin.sdk.nanj.listener

import java.math.BigDecimal

/**
 * Listener is used for NANJ Rate Request to remote server
 */
interface NANJRateListener {

    /**
     * on success get rate
     * @param value as BigDecimal
     */
    fun onSuccess(value : BigDecimal)

    /**
     * on failed get rate
     * @param error as String
     */
    fun onFailure(error : String)
}