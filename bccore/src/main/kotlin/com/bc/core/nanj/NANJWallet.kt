package com.bc.core.nanj

import android.app.Activity
import android.content.Context
import android.support.v4.app.Fragment
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bc.core.ui.barcodereader.NANJQrCodeActivity
import com.bc.core.ui.nfc.NANJNfcActivity
import com.bc.core.util.*
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.Type
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.utils.Numeric
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutionException
import java.io.BufferedReader
import java.io.InputStreamReader

//web3j solidity generate MetaNANJCOINManager.bin MetaNANJCOINManager.abi -o . -p org.your.package

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
        const val WALLET_ADDRESS = "WALLET_ADDRESS"
        const val QRCODE_REQUEST_CODE: Int = 10001
        const val QRCODE_RESULT_CODE: Int = 10003
        const val NFC_REQUEST_CODE: Int = 10002
        const val NFC_RESULT_CODE: Int = 10004
    }

    var address: String = ""
    var name: String = "No name"
    var privatekey: String? = null
    var cridentals: Credentials? = null
    private lateinit var _web3j: Web3j
    var web3j: Web3j? = null
        set(value) {
            _web3j = value ?: return
        }
    private var nanjSmartContract: NANJSmartContract? = null
    private var txRelay: TxRelay? = null
    private var metaNANJCOINManager: MetaNANJCOINManager? = null

    fun init() {
        initNANJSmartContract()
        initMetaNANJCOINManager()
        initTxRelay()
    }

    private fun initNANJSmartContract() {
        this.nanjSmartContract = NANJSmartContract.load(
                NANJConfig.SMART_CONTRACT_ADDRESS,
                _web3j,
                cridentals!!
        )
    }

    private fun initTxRelay() {
        this.txRelay = TxRelay.load(
                TX_RELAY_ADDRESS,
                _web3j,
                cridentals!!
        )
    }

    private fun initMetaNANJCOINManager() {
        this.metaNANJCOINManager = MetaNANJCOINManager.load(
                NANJCOIN_ADDRESS,
                _web3j,
                cridentals!!
        )
    }

    fun getAmountEth(): BigInteger {
        return try {
            _web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                    .send()
                    .balance
        } catch (e: Exception) {
            e.printStackTrace()
            ZERO_AMOUNT_NANJ_COIN
        }
    }

    fun getNANJCoinObservable() = nanjSmartContract?.balanceOf(address)?.observable()

    fun getAmountNanj(): BigInteger {
        return try {
            nanjSmartContract?.balanceOf(address)?.send()
                    ?: ZERO_AMOUNT_NANJ_COIN
        } catch (e: Exception) {
            e.printStackTrace()
            ZERO_AMOUNT_NANJ_COIN
        }
    }

    fun getTransactions(page: Int, offset: Int = 20, listener: NANJTransactionsListener) {
        doAsync(
                {
                    it.printStackTrace()
                    uiThread { listener.onTransferFailure() }
                },
                {
                    getNANJWallet()
                    //testEncodeFunction(context)
                    //testSign()
                    val url = URL(
                            String.format(
                                    NANJConfig.URL_TRANSACTION,
                                    nanjSmartContract?.contractAddress,
                                    address,
                                    page,
                                    offset
                            )
                    )
                    println("nanj transactions :  $url")
                    val httpURLConnection = url.openConnection() as HttpURLConnection
                    val stringBuilder = StringBuilder()
                    val transactionList: TransactionResponse?
                    try {
                        val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                        var line: String? = bufferedReader.readLine()
                        while (line != null) {
                            stringBuilder.append(line).append("\n")
                            line = bufferedReader.readLine()
                        }
                        bufferedReader.close()
                        val toString = stringBuilder.toString()
                        println(toString)
                        transactionList = Gson().fromJson(toString, TransactionResponse::class.java)
                    } finally {
                        httpURLConnection.disconnect()
                    }
                    uiThread { listener.onTransferSuccess(transactionList?.transactions) }
                }
        )
    }

    fun getNANJWallet(listener: GetNANJWalletListener ?= null) {
        doAsync(
                {
                    it.printStackTrace()
                    uiThread { listener?.onError() }
                },
                {
                    val nanjWallet = metaNANJCOINManager!!.getWallet(address).send()
                    uiThread { listener?.onSuccess(nanjWallet) }
                }
        )
    }

    fun createNANJWallet(listener : CreateNANJWalletListener ?= null) {
        doAsync (
                {
                    it.printStackTrace()
                    uiThread { listener?.onError() }
                },
                {
                    val param = arrayListOf<Type<*>>(Address(address))
                    val f = Function("createWallet", param, arrayListOf())
                    val data = FunctionEncoder.encode(f)
                    val sign = Sign.signMessage(data.toByteArray(), cridentals!!.ecKeyPair)
                    val nonceString = "0"//nonce.toString(16)
                    val pad = nonceString.padStart(64, '0')
                    val hashInput = "0x1900${TX_RELAY_ADDRESS.replace("0x", "")}${WALLET_OWNER.replace("0x", "")}$pad${NANJCOIN_ADDRESS.replace("0x", "")}${data.replace("0x", "")}"
                    val hash = Hash.sha3(hashInput)
                    val restData = Test(
                            Numeric.toHexString(sign.r),
                            Numeric.toHexString(sign.s),
                            sign.v.toString(),
                            data,
                            pad,
                            NANJCOIN_ADDRESS,//dest
                            hash
                    )
                    val jsonObject = JSONObject(Gson().toJson(restData).toString())
                    val request = JsonObjectRequest(
                            Request.Method.POST,
                            "https://nanj-demo.herokuapp.com/api/relayTx",
                            jsonObject,
                            Response.Listener { response ->
                                println("response --> $response")
                            },
                            Response.ErrorListener { it.printStackTrace() })
                    Volley.newRequestQueue(null).add(request)
                }
        )
    }



    private fun testSign() {
        val msg = "Hello World".toByteArray()
        val p = "d8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
        val pub = Sign.publicKeyFromPrivate(BigInteger(p, 16))
        println("rrr ->  $pub")
        val hash = msg
        val s = Numeric.hexStringToByteArray("0x6612d114ac4769ad9968eb014a6029da381328106e17e48f58c3a256542ee85e")
        val r = Numeric.hexStringToByteArray("0x26cdc16571a273bcba4a3160bfc499e791757ea3e79c3caef29465f2e886291c")
        val v = 27.toByte()
        val sd = Sign.SignatureData(v, r, s)
        val dd = Sign.signedMessageToKey(hash, sd)
        println("wtf ${dd}")
        println("wtf ${dd.toString(16)}")
    }

    fun testEncodeFunction(context: Context) {
        val param = arrayListOf<Type<*>>(
                Address(address)
        )

        val f = Function(
                "createWallet",
                param,
                arrayListOf()
        )
        val nonce = _web3j.ethGetTransactionCount(address, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get()
                .transactionCount
        println("nonce $nonce")
        val data = FunctionEncoder.encode(f)
        println("encode function $data")
        val sign = Sign.signMessage(data.toByteArray(), cridentals!!.ecKeyPair)
        val nonceString = "0"//nonce.toString(16)
        val pad = nonceString.padStart(64, '0')
        println("pad $pad")
        val hashInput = "0x1900${TX_RELAY_ADDRESS.replace("0x", "")}${WALLET_OWNER.replace("0x", "")}$pad${NANJCOIN_ADDRESS.replace("0x", "")}${data.replace("0x", "")}"
        println("hashInput $hashInput")
        val hash = Hash.sha3(hashInput)
        println("hash $hash")
        val restData = Test(
                Numeric.toHexString(sign.r),
                Numeric.toHexString(sign.s),
                sign.v.toString(),
                data,
                pad,
                NANJCOIN_ADDRESS,//dest
                hash
        )
        println("data ${Gson().toJson(restData)}")
        val lastSign = Hash.sha3(Gson().toJson(restData))
        val abc = _web3j.ethSendRawTransaction(lastSign)
                .send()
        println("response ${abc.transactionHash}")
        val volley = Volley.newRequestQueue(context)
        val jsonObject = JSONObject(Gson().toJson(restData).toString())
        val request = JsonObjectRequest(
                Request.Method.POST,
                "https://nanj-demo.herokuapp.com/api/relayTx",
                jsonObject,
                Response.Listener {
                    println("aaaa $it")
                },
                Response.ErrorListener { it.printStackTrace() })
        volley.add(request)
    }


    fun sentNANJCoin(toAddress: String, amount: String, transferListener: NANJTransactionListener) {
        doAsync(
                {
                    println("my wallet transfer error")
                    it.printStackTrace()
                    uiThread { transferListener.onTransferFailure() }
                },
                {
                    nanjSmartContract?.transfer(toAddress, BigInteger(amount))?.send()
                    uiThread { transferListener.onTransferSuccess() }
                }
        )
    }

    @Throws(InterruptedException::class, ExecutionException::class)
    private fun getNonce(address: String): BigInteger {
        web3j?.let {
            val ethGetTransactionCount = it.ethGetTransactionCount(address, DefaultBlockParameterName.PENDING).sendAsync().get()
            return ethGetTransactionCount.transactionCount
        }
        return ZERO_AMOUNT_NANJ_COIN
    }

    fun sendNANJCoinByQrCode(activity: Activity) {
        activity.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
    }

    fun sendNANJCoinByQrCode(fragment: Fragment) {
        fragment.launchActivity<NANJQrCodeActivity>(QRCODE_REQUEST_CODE)
    }

    fun sendNANJCoinByNfcCode(activity: Activity) {
        activity.launchActivity<NANJNfcActivity>(NFC_REQUEST_CODE)
    }

    fun sendNANJCoinByNfcCode(fragment: Fragment) {
        fragment.launchActivity<NANJNfcActivity>(NFC_REQUEST_CODE)
    }
}
 