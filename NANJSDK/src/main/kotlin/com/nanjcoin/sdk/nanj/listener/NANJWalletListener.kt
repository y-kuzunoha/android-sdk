package com.nanjcoin.sdk.nanj.listener


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/16/18
 * ____________________________________
 */
interface NANJWalletListener {
	fun onCreateWalletSuccess(privateKey: String)
	fun onCreateWalletFailure()
	fun onImportWalletSuccess()
	fun onImportWalletFailure()
}