package com.nanjcoin.sdk.nanj.listener

import com.nanjcoin.sdk.model.TransactionPage


/**
 * NANJ transactions listener for retrieving list of transactions
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/16/18
 * ____________________________________
 */
interface NANJTransactionsListener {

	/**
	 * on loaded transactions
	 * @param transactions as TransactionPage
	 */
	fun onLoadedTransactions(transactions: TransactionPage?)

	/**
	 * on failed load
	 */
	fun onFailure()
}