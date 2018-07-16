package com.bc.core.nanj.listener

import java.math.BigDecimal

interface NANJRateListener {
    fun onSuccess(values : BigDecimal)
    fun onFailure(e : String)
}