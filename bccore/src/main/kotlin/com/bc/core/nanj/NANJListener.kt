package com.bc.core.nanj

import org.web3j.crypto.ContractUtils
import org.web3j.ens.Contracts
import org.web3j.tx.Contract

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/24/18
 * ____________________________________
 */

interface NANJCreateWalletListener {
	fun onCreateWalletSuccess(backup: String)
	fun onCreateWalletFailure()
}

interface NANJImportWalletListener {
	fun onImportWalletSuccess()
	fun onImportWalletFailure()
}