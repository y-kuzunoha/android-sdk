package com.bc.core.nanj.listener

import com.bc.core.nanj.Transaction


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/16/18
 * ____________________________________
 */
interface NANJTransactionsListener {
	fun onTransferSuccess(transactions: MutableList<Transaction>?)
	fun onTransferFailure()
}