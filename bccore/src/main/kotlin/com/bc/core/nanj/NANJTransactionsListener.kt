package com.bc.core.nanj


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