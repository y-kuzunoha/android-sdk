package com.bc.core.nanj

import com.bc.core.util.uiThread
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
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

class NANJWalletManager constructor(private val nanjWalletListener : NANJWalletListener) {

	private lateinit var _wallet : NANJWallet
	private var _wallets : MutableMap<String, NANJWallet> = mutableMapOf()

	fun getWallet() : NANJWallet = _wallet

	fun getWallets() : MutableList<NANJWallet> = _wallets.values.toMutableList()

	fun addWallet(wallet : NANJWallet) {
		_wallets.put(wallet.getAddress(), wallet)
	}

	fun importWallet(password : String, source : File) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val credentials = WalletUtils.loadCredentials(password, source)
				importWalletFromCredentials(credentials)
				uiThread { nanjWalletListener.onImportWalletSuccess() }
			}
		)
	}

	fun importWallet(password : String, jsonWallet : String) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val objectMapper = ObjectMapper().apply {
					this.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
					this.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				}
				val walletFile = objectMapper.readValue(jsonWallet, WalletFile::class.java)
				val credentials = Credentials.create(Wallet.decrypt(password, walletFile))
				importWalletFromCredentials(credentials)
				uiThread { nanjWalletListener.onImportWalletSuccess() }
			}
		)
	}

	fun importWallet(privateKey : String) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val credential = Credentials.create(privateKey)
				importWalletFromCredentials(credential)
				uiThread { nanjWalletListener.onImportWalletSuccess() }
			}
		)

	}

	private fun importWalletFromCredentials(credentials : Credentials) {
		val nanjWallet = NANJWallet().apply {
			setAddress(credentials.address)
		}
		_wallets[nanjWallet.getAddress()] = nanjWallet
	}

	fun removeWallet(position : Int) {
		val key = _wallets.keys.toMutableList()[position]
		_wallets.remove(key)
	}

	fun createWallet(password : String, destinationDirectory : File) {
		doAsync(
			{ uiThread { nanjWalletListener.onCreateWalletFailure() } },
			{
				val addressWallet = WalletUtils.generateNewWalletFile(
					password,
					destinationDirectory,
					false
				)
				val pathWallet = "${destinationDirectory.path}/$addressWallet"
				val credentials = WalletUtils.loadCredentials(password, pathWallet)
				println("address    ----------->  ${credentials.address}")
				println("privateKey ----------->  ${credentials.ecKeyPair.privateKey}")
				println("publicKey  ----------->  ${credentials.ecKeyPair.publicKey}")
				importWalletFromCredentials(credentials)
				uiThread { nanjWalletListener.onCreateWalletSuccess(credentials.ecKeyPair.privateKey.toString()) }
			}
		)
	}

	fun enableWallet(wallet : NANJWallet) {
		this._wallet = wallet
	}

	fun enableWallet(position : Int) {
	}

	fun getNanjRate() : Double = 0.0

}