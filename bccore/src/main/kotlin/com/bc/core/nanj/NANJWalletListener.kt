package com.bc.core.nanj


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/16/18
 * ____________________________________
 */
interface NANJWalletListener {
	fun onCreateWalletSuccess()
	fun onCreateWalletFailure()
	fun onImportWalletSuccess()
	fun onImportWalletFailure()
}