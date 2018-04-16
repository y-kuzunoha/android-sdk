package com.bc.core.nanj

import org.web3j.crypto.WalletUtils
import java.io.File
import java.security.KeyStore

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NanjWalletManager {
	private lateinit var _wallet: NanjWallet
	private  var _wallets: MutableList<NanjWallet> = mutableListOf()

	fun getWallet(): NanjWallet = _wallet

	fun getWallets(): MutableList<NanjWallet> = _wallets

	fun addWallet(wallet : NanjWallet) {
		_wallets.add(wallet)
	}

	fun addWallet(jsonWallet : String) {
		TODO()
	}

	fun addWallet(walletKeystore : KeyStore) {
		TODO()
	}

	fun removeWallet(position: Int) {
		_wallets.removeAt(position)
	}

	fun createWallet(password: String, destinationDirectory : File) {
		//val file = Environment.getExternalStorageDirectory().absoluteFile
		val addressWallet = WalletUtils.generateNewWalletFile(
			password,
			destinationDirectory,
			false
		)
		println(addressWallet)
	}

	fun enableWallet(wallet : NanjWallet) {
		this._wallet = wallet
	}

	fun enableWallet(position : Int) {
		this._wallet = this._wallets[position]
	}

	fun getNanjRate(): Double = 0.0

}