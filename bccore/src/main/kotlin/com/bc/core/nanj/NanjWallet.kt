package com.bc.core.nanj


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NanjWallet constructor(private val nanjTransactionListener : NanjTransactionListener? = null) {
	private var _address : String = ""
	private var _name : String = ""
	private var _amoutEth : Double = 0.0
	private var _amountNanj : Double = 0.0

	fun editName(newName : String) {
		this._name = newName
	}

	fun getAmountEth() = _amoutEth

	fun getAmountNanj() = _amountNanj

	fun getTransactions() : List<NanjTransaction> = emptyList()

	fun sendNanjCoin(toWallet : NanjWallet, amount : Double) {

	}

	fun sentNanjCoin(address : String, amount : Double) {

	}
}
 