package com.bc.core.nanj

import android.content.Context
import com.bc.core.database.NANJDatabase
import com.bc.core.util.uiThread
import com.fasterxml.jackson.databind.ObjectMapper
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.web3j.crypto.Credentials
import org.web3j.crypto.Keys
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWalletManager constructor(context : Context) {

	private var wallets : MutableMap<String, NANJWallet> = mutableMapOf()
	private val _nanjDatabase = NANJDatabase(context)
	private val _web3j = Web3jFactory.build(HttpService("https://rinkeby.infura.io/1Sxab6iBbbiFHwtnbZfO"))
	var wallet : NANJWallet? = null

	init {
		wallets = _nanjDatabase.loadWallets()
		if (wallets.isNotEmpty()) {
			enableWallet(0)
		}
	}

	fun addWallet(wallet : NANJWallet) {
		wallets[wallet.address] = wallet
	}

	fun getWalletList() : MutableList<NANJWallet> = wallets.values.toMutableList()

	/**
	 * Import a wallet from keystore
	 *
	 * @param password
	 * @param fileKeystore
	 *
	 * */
	fun importWallet(password : String, fileKeystore : File, nanjWalletListener : NANJImportWalletListener) {
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
	fun importWallet(password : String, jsonWallet : String, nanjWalletListener : NANJImportWalletListener) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val objectMapper = ObjectMapper()
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
	fun importWallet(privateKey : String, nanjWalletListener : NANJImportWalletListener) {
		doAsync(
			{ uiThread { nanjWalletListener.onImportWalletFailure() } },
			{
				val credential = Credentials.create(privateKey)
				importWalletFromCredentials(credential)
				uiThread { nanjWalletListener.onImportWalletSuccess() }
			}
		)

	}

	private fun importWalletFromCredentials(credentials : Credentials) : NANJWallet {
		val nanjWallet = NANJWallet().apply {
			address = credentials.address
		}
		wallets[nanjWallet.address] = nanjWallet
		_nanjDatabase.saveWallet(nanjWallet)
		return nanjWallet
	}

	fun removeWallet(position : Int) {
		val key = wallets.keys.toMutableList()[position]
		wallets.remove(key)
	}
	fun removeWallet(wallet : NANJWallet) {
		_nanjDatabase.removeWallet(wallet)
		wallets.remove(wallet.address)
	}

	/**
	 * Create a wallet.
	 *
	 * @param password
	 *
	 * */
	fun createWallet(password : String, createWalletListener : NANJCreateWalletListener) {
		doAsync(
			{ uiThread { createWalletListener.onCreateWalletFailure() } },
			{
				val ecKeyPair = Keys.createEcKeyPair()
				val addressWallet = Wallet.createLight(password, ecKeyPair)
				val credentials = Credentials.create(Wallet.decrypt(password, addressWallet))
				val wallet = importWalletFromCredentials(credentials)
				val objectMapper = ObjectMapper()
				uiThread { createWalletListener.onCreateWalletSuccess(objectMapper.writeValueAsString(addressWallet), wallet) }
			}
		)
	}

	fun enableWallet(wallet : NANJWallet) {
		this.wallet = wallet.apply {
			this.web3j = _web3j
		}
	}

	fun enableWallet(position : Int) {
		if (position < 0 || position >= wallets.size) return // return if out size of list
		val list = wallets.keys.toMutableList()
		val key = list[position]
		val wallet = wallets[key]!!
		this.enableWallet(wallet)
	}

	fun getNANJRate() : Double {
		return 0.0
	}
}