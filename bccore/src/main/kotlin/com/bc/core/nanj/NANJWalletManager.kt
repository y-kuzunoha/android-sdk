package com.bc.core.nanj

import android.content.Context
import com.bc.core.database.NANJDatabase
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

class NANJWalletManager constructor(context : Context, private val nanjWalletListener : NANJWalletListener) {

	private var _wallets : MutableMap<String, NANJWallet> = mutableMapOf()
	private val _nanjDatabase = NANJDatabase(context)

	private lateinit var _wallet : NANJWallet

	init {
		_wallets = _nanjDatabase.loadWallets()
	}

	fun getWallet() : NANJWallet = _wallet

	fun getWallets() : MutableList<NANJWallet> = _wallets.values.toMutableList()

	fun addWallet(wallet : NANJWallet) {
		_wallets[wallet.getAddress()] = wallet
	}

	/**
	 * Import a wallet from keystore
	 *
	 * @param password
	 * @param fileKeystore
	 *
	 * */
	fun importWallet(password : String, fileKeystore : File) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val credentials = WalletUtils.loadCredentials(password, fileKeystore)
				importWalletFromCredentials(credentials)
				uiThread { nanjWalletListener.onImportWalletSuccess() }
			}
		)
	}

	/**
	 * Import a wallet from json
	 *
	 * @param password
	 * @param jsonWallet
	 *
	 * */
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

	/**
	 * Import a wallet from private key
	 *
	 * @param privateKey
	 *
	 * */
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

		_nanjDatabase.saveWallet(nanjWallet)
	}

	fun removeWallet(position : Int) {
		val key = _wallets.keys.toMutableList()[position]
		_wallets.remove(key)
	}

	/**
	 * Create a wallet.
	 *
	 * @param password
	 * @param destinationDirectory
	 *
	 * */
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