package com.bc.core.nanj

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.web3j.crypto.Credentials
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile
import org.web3j.crypto.WalletUtils
import java.io.File

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWalletManager {
	
	private lateinit var _wallet: NANJWallet
	private  var _wallets: MutableMap<String, NANJWallet> = mutableMapOf()

	fun getWallet(): NANJWallet = _wallet

	fun getWallets(): MutableList<NANJWallet> = _wallets.values.toMutableList()

	fun addWallet(wallet : NANJWallet) {
		_wallets.put(wallet.getAddress(), wallet)
	}

	fun importWallet(password : String, source: File) {
		val credential = WalletUtils.loadCredentials(password, source)
	}

	fun importWallet(password : String, jsonWallet : String) {
		val objectMapper = ObjectMapper().apply {
			this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
			this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
		} 
		val walletFile = objectMapper.readValue(jsonWallet, WalletFile::class.java)
		val credential = Credentials.create(Wallet.decrypt(password, walletFile))
	}

	fun importWallet(privateKey : String) {
		val credential = Credentials.create(privateKey)
	}

	fun removeWallet(position: Int) {
		val key = _wallets.keys.toMutableList()[position]
		_wallets.remove(key)
	}

	fun createWallet(password: String, destinationDirectory : File) {
		val addressWallet = WalletUtils.generateNewWalletFile(
			password,
			destinationDirectory,
			false
		)

		val pathWallet = "${destinationDirectory.path}/$addressWallet"

		println("------------------------------------")

		println(pathWallet)

		val credential = WalletUtils.loadCredentials(password, pathWallet)

		println(credential.address)

		println(credential.ecKeyPair.privateKey.toString(16))
		println(credential.ecKeyPair.publicKey.toString(16))
		
		val nanjWallet = NANJWallet().apply {
			setAddress(credential.address)
		}
		
		_wallets[nanjWallet.getAddress()] = nanjWallet

		println("------------------------------------")
	}

	fun enableWallet(wallet : NANJWallet) {
		this._wallet = wallet
	}

	fun enableWallet(position : Int) {
	}

	fun getNanjRate(): Double = 0.0

}