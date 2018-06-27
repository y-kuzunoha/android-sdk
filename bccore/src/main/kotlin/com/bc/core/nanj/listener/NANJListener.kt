package com.bc.core.nanj.listener

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/24/18
 * ____________________________________
 */

interface NANJCreateWalletListener {
	fun onCreateProcess(backup: String?)
	fun onCreateWalletFailure()
}

interface NANJImportWalletListener {
	fun onImportWalletSuccess()
	fun onImportWalletFailure()
}