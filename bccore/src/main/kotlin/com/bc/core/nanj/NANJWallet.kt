package com.bc.core.nanj

import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import java.math.BigInteger


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWallet constructor(private val nanjTransactionListener : NANJTransactionListener? = null) {

	var address : String = ""
	var name : String = "Noname"
	var amountNanj : Double = 0.0
	private lateinit var _web3j : Web3j
	var web3j : Web3j?
		get() = null
		set(value) {
			if (value != null) {
				_web3j = value
			}
		}

	fun getAmountEth() : BigInteger {
		val getBalance : EthGetBalance = _web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
			.sendAsync()
			.get()
		return getBalance.balance
	}

	fun getTransactions() : List<NANJTransaction> = emptyList()

	fun sendNanjCoin(toWallet : NANJWallet, amount : Double) {

	}

	fun sentNanjCoin(address : String, amount : Double) {

	}
}
 