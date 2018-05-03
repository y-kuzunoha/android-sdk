package com.bc.core.nanj

import android.app.Activity
import android.support.v4.app.Fragment
import com.bc.core.ui.barcodereader.NANJQrCodeActivity
import com.bc.core.util.launchActivity
import org.jetbrains.anko.doAsync
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
			val getBalance : EthGetBalance = _web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
				.sendAsync()
				.get()
			getBalance.balance
		} catch (e : Exception) {
			e.printStackTrace()
			BigInteger("0")
		}
	}

	fun getNANJCoinObservable() = contract?.balanceOf(address/*"0x5d4d563195b0141476a834533dd6e097c0707751"*/)?.observable()
	fun getNANJCoin() = contract?.balanceOf(address/*"0x5d4d563195b0141476a834533dd6e097c0707751"*/)?.sendAsync()

	fun getTransactions() : List<NANJTransaction> = emptyList()

	fun sentNANJCoin(toAddress : String, amount : String) {
		doAsync(
			{
				it.printStackTrace()
			},
			{
				println("my wallet validate contract          -> ${contract?.isValid}")
				val transactionReceipt = contract?.transfer("0x70329776934Dc953eb01200D6E8473dBe054f665", BigInteger("1"))?.send()
				println("my wallet transaction status           -> ${transactionReceipt?.status}")
				println("my wallet transaction block hash       -> ${transactionReceipt?.blockHash}")
				println("my wallet transaction contract address -> ${transactionReceipt?.contractAddress}")
				println("my wallet transaction to               -> ${transactionReceipt?.to}")
				println("my wallet transaction from             -> ${transactionReceipt?.from}")
			}
		)
	}

	@Throws(InterruptedException::class, ExecutionException::class)
	private fun getNonce(address : String) : BigInteger {
		web3j?.let {
			val ethGetTransactionCount = it.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).sendAsync().get()
			return ethGetTransactionCount.transactionCount
		}
		return BigInteger("0")
	}

	fun sendNANJCoinByQrCode(activity : Activity) {
		activity.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
	}

	fun sendNANJCoinByQrCode(fragment : Fragment) {
		fragment.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
	}
}
 