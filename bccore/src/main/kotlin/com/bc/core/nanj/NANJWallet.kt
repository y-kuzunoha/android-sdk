package com.bc.core.nanj

import android.app.Activity
import android.support.v4.app.Fragment
import com.bc.core.ui.barcodereader.NANJQrCodeActivity
import com.bc.core.ui.nfc.NANJNfcActivity
import com.bc.core.util.launchActivity
import com.bc.core.util.uiThread
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.response.EthGetBalance
import java.math.BigInteger
import java.util.concurrent.ExecutionException

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWallet {

	companion object {
		private val ZERO_AMOUNT_NANJ_COIN = BigInteger.valueOf(0)
		private val REAL_AMOUNT_NANJ_COIN = BigInteger.valueOf(1_000_000_000)
		private val REAL_AMOUNT_ETH_COIN = BigInteger.valueOf(1_000_000_000_000_000_000)

		const val WALLET_ADDRESS = "WALLET_ADDRESS"
		const val QRCODE_REQUEST_CODE : Int = 10001
		const val QRCODE_RESULT_CODE : Int = 10003
		const val NFC_REQUEST_CODE : Int = 10002
		const val NFC_RESULT_CODE : Int = 10004
	}

	var address : String = ""
	var name : String = "No name"
	var privatekey : String? = null
	var cridentals : Credentials? = null
	var amountNanj : Double = 0.0
	private lateinit var _web3j : Web3j
	var web3j : Web3j? = null
		set(value) {
			_web3j = value ?: return
		}
	var contract : NANJSmartContract? = null

	fun getAmountEth() : BigInteger {
		return try {
			_web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
				.sendAsync()
				.get()
				.balance
		} catch (e : Exception) {
			e.printStackTrace()
			ZERO_AMOUNT_NANJ_COIN
		}
	}

	fun getNANJCoinObservable() = contract?.balanceOf(address)?.observable()
	fun getNANJCoin() : BigInteger {
		return  try {
			contract?.balanceOf(address)?.sendAsync()?.get()
				?: ZERO_AMOUNT_NANJ_COIN
		} catch (e : Exception) {
			e.printStackTrace()
			ZERO_AMOUNT_NANJ_COIN
		}
	}

	fun getTransactions() : List<NANJTransaction> = emptyList()

	fun sentNANJCoin(toAddress : String, amount : String, transferListener : NANJTransactionListener) {
		doAsync(
			{
				println("my wallet transfer error")
				it.printStackTrace()
				uiThread { transferListener.onTransferFailure() }
			},
			{
				println("my wallet transfer $toAddress")
				println("my wallet transfer $amount")
				val transactionReceipt = contract?.transfer(toAddress, BigInteger(amount))?.send()
				println("my wallet transaction status           -> ${transactionReceipt?.status}")
				println("my wallet transaction block hash       -> ${transactionReceipt?.blockHash}")
				println("my wallet transaction contract address -> ${transactionReceipt?.contractAddress}")
				println("my wallet transaction to               -> ${transactionReceipt?.to}")
				println("my wallet transaction from             -> ${transactionReceipt?.from}")
				uiThread { transferListener.onTransferSuccess() }
			}
		)
	}

	@Throws(InterruptedException::class, ExecutionException::class)
	private fun getNonce(address : String) : BigInteger {
		web3j?.let {
			val ethGetTransactionCount = it.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).sendAsync().get()
			return ethGetTransactionCount.transactionCount
		}
		return ZERO_AMOUNT_NANJ_COIN
	}

	fun sendNANJCoinByQrCode(activity : Activity) {
		activity.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
	}

	fun sendNANJCoinByQrCode(fragment : Fragment) {
		fragment.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
	}

	fun sendNANJCoinByNfcCode(activity : Activity) {
		activity.launchActivity<NANJNfcActivity>(NFC_REQUEST_CODE)
	}

	fun sendNANJCoinByNfcCode(fragment : Fragment) {
		fragment.launchActivity<NANJNfcActivity>(NFC_REQUEST_CODE)
	}
}
 