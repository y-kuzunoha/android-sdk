package com.nanjcoin.sdk.nanj.listener

import com.nanjcoin.sdk.model.DataTransaction


/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/16/18
 * ____________________________________
 */
interface NANJTransactionsListener {
	fun onTransferSuccess(transactions: DataTransaction?)
	fun onTransferFailure()
}