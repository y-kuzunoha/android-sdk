package com.bc.core.nanj.listener

import com.bc.core.nanj.NANJWallet

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/24/18
 * ____________________________________
 */

interface NANJCreateWalletListener {
	fun onCreateWalletSuccess(backup: String, wallet: NANJWallet)
	fun onCreateWalletFailure()
}

interface NANJImportWalletListener {
	fun onImportWalletSuccess()
	fun onImportWalletFailure()
}