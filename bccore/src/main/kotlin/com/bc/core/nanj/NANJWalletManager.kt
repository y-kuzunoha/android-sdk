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

class NANJWalletManager constructor(context : Context, private val nanjWalletListener : NANJWalletListener) {

	var wallets : MutableMap<String, NANJWallet> = mutableMapOf()
	private val _nanjDatabase = NANJDatabase(context)
	private val _web3j  = Web3jFactory.build(HttpService("https://rinkeby.infura.io/1Sxab6iBbbiFHwtnbZfO"))
	lateinit var wallet : NANJWallet

	init {
		wallets = _nanjDatabase.loadWallets()
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
			address = credentials.address
		}
		wallets[nanjWallet.address] = nanjWallet

		_nanjDatabase.saveWallet(nanjWallet)
	}

	fun removeWallet(position : Int) {
		val key = wallets.keys.toMutableList()[position]
		wallets.remove(key)
	}

	/**
	 * Create a wallet.
	 *
	 * @param password
	 *
	 * */
	fun createWallet(password : String) {
		doAsync(
			{ uiThread { nanjWalletListener.onCreateWalletFailure() } },
			{
				val ecKeyPair = Keys.createEcKeyPair()
				val addressWallet = Wallet.createLight(password, ecKeyPair)
				val credentials = Credentials.create(Wallet.decrypt(password, addressWallet))
				importWalletFromCredentials(credentials)
				val objectMapper = ObjectMapper()
				uiThread { nanjWalletListener.onCreateWalletSuccess(objectMapper.writeValueAsString(addressWallet)) }
			}
		)
	}

	fun enableWallet(wallet : NANJWallet) {
		this.wallet = wallet
		this.wallet.web3j = _web3j
	}

	fun enableWallet(position : Int) {
		val list = wallets.keys.toMutableList()
		if(position >= 0 && position < list.size) return
		val key = list[position]
		val wallet = wallets[key] ?: return
		this.enableWallet(wallet)
	}

	fun getNANJRate() : Double {
		return 0.0
	}
}