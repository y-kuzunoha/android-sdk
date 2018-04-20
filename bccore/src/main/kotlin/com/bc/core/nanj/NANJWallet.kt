package com.bc.core.nanj


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWallet constructor(private val nanjTransactionListener : NANJTransactionListener? = null) {
	private var _address : String = ""
	private var _name : String = "Noname"
	private var _amoutEth : Double = 0.0
	private var _amountNanj : Double = 0.0

	fun editName(newName : String) {
		this._name = newName
	}
	
	fun setName(name: String) {
		this._name = name
	}
	
	fun getName() = _name
	
	fun setAddress(address : String) {
		this._address = address
	}
	fun getAddress() = _address

	fun getAmountEth() = _amoutEth

	fun getAmountNanj() = _amountNanj

	fun getTransactions() : List<NANJTransaction> = emptyList()

	fun sendNanjCoin(toWallet : NANJWallet, amount : Double) {

	}

	fun sentNanjCoin(address : String, amount : Double) {

	}
}
 