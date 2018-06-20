package com.bc.core.nanj

import android.app.Activity
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.bc.core.database.NANJDatabase
import com.bc.core.model.TransactionResponse
import com.bc.core.model.TxRelayData
import com.bc.core.nanj.listener.*
import com.bc.core.ui.barcodereader.NANJQrCodeActivity
import com.bc.core.ui.nfc.NANJNfcActivity
import com.bc.core.util.*
import com.google.gson.Gson
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import org.jetbrains.anko.doAsync
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.*
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.utils.Numeric
import java.math.BigInteger

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
        const val WALLET_ADDRESS = "WALLET_ADDRESS"
        const val QRCODE_REQUEST_CODE: Int = 10001
        const val QRCODE_RESULT_CODE: Int = 10003
        const val NFC_REQUEST_CODE: Int = 10002
        const val NFC_RESULT_CODE: Int = 10004
    }

    var nanjDatabase: NANJDatabase? = null

    var nanjAddress: String? = ""
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
                NANJConfig.TX_RELAY_ADDRESS,
                _web3j,
                cridentals!!
        )
    }

    private fun initMetaNANJCOINManager() {
        this.metaNANJCOINManager = MetaNANJCOINManager.load(
                web3j = _web3j,
                credentials = cridentals!!
        )
    }

    fun getAmountNanj(): BigInteger {
        val nanjAddress = getNANJWallet()
        println("getAmountNanj    $nanjAddress")
        if(nanjAddress == NANJConfig.UNKNOWN_NANJ_WALLET) return BigInteger.ZERO
        return nanjSmartContract?.balanceOf(nanjAddress)?.send()
                ?: BigInteger.ZERO
    }

    fun getTransactions(page: Int, offset: Int = 20, listener: NANJTransactionsListener) {
        //getNANJWallet()
        //testEncodeFunction(context)
        //testSign()
        doAsync(
                {
                    listener.onTransferFailure()
                    it.printStackTrace()
                },
                {
                    val nanjAddress = getNANJWallet()
                    val url = String.format(
                            NANJConfig.URL_TRANSACTION,
                            "0xf7afb89bef39905ba47f3877e588815004f7c861"/*nanjSmartContract?.contractAddress*/,
                            nanjAddress,
                            page,
                            offset
                    )
                    NetworkUtil.retofit.create(Api::class.java)
                            .getNANJTransactions(url)
                            .subscribeOn(Schedulers.computation())
                            .subscribe(
                                    {
                                        val transactionList: TransactionResponse? = Gson().fromJson(it.string(), TransactionResponse::class.java)
                                        listener.onTransferSuccess(transactionList?.transactions)
                                    },
                                    {
                                        it.printStackTrace()
                                         listener.onTransferFailure()
                                    }
                            )
                }
        )

    }

    fun getNANJWalletAsync(listener: GetNANJWalletListener) {
        doAsync(
                {   listener.onError()  },
                {
                    val address = getNANJWallet()
                      listener.onSuccess(address)
                }
        )
    }

    fun getNANJWallet(): String {
        if (!TextUtils.isEmpty(nanjAddress)) return nanjAddress!!
        val address = metaNANJCOINManager!!.getWallet(address).send()
        if (NANJConfig.UNKNOWN_NANJ_WALLET != address) {
            nanjAddress = address
            nanjDatabase?.saveWallet(this)
        }
        return address
    }

    fun createNANJWallet(listener: CreateNANJWalletListener? = null) {
        doAsync(
                {
                    it.printStackTrace()
                    listener?.onError()
                },
                {
                    val param = arrayListOf<Type<*>>(Address(address))
                    val createNanjWalletFunction = Function("createWallet", param, arrayListOf())
                    sendFunctionToServer(
                            createNanjWalletFunction,
                            error = { listener?.onError() },
                            success = { listener?.onCreateProcess() }
                    )
                }
        )
    }

    fun sendNANJCoin(toAddress: String, amount: String, listener: SendNANJCoinListener? = null) {
        doAsync(
                {
                    it.printStackTrace()
                    listener?.onError()
                },
                {
                    val nanjAddress = metaNANJCOINManager!!.getWallet(address).send()
                    val param = arrayListOf<Type<*>>(
                            Address(toAddress),
                            Uint256(BigInteger(amount).multiply(BigInteger.TEN.pow(8)))
                    )
                    val f = Function("transfer", param, arrayListOf())
                    val data = FunctionEncoder.encode(f)
                    val hexData = Numeric.hexStringToByteArray(data)
                    val params = arrayListOf<Type<*>>(
                            Address(address),
                            Address(nanjAddress),
                            Address(NANJConfig.NANJCOIN_ADDRESS),
                            Uint256.DEFAULT,
                            DynamicBytes(hexData)
                    )
                    val forwardToFunction = Function("forwardTo", params, arrayListOf())
                    sendFunctionToServer(
                            forwardToFunction,
                            error = { listener?.onError() },
                            success = { listener?.onSuccess() }
                    )
                }
        )
    }

    private fun sendFunctionToServer(function: Function, error: () -> Unit, success: () -> Unit) {
        val encodeFunction = FunctionEncoder.encode(function)
        val sign = Sign.signMessage(Numeric.hexStringToByteArray(encodeFunction), cridentals!!.ecKeyPair)
        val nanjAddress = metaNANJCOINManager!!.getWallet(address).send()
        val nonceString = txRelay!!.getNonce(nanjAddress).send().toString(16).replace("0x", "")
        val pad = nonceString.padStart(64, '0')
        val hashInput = "0x1900${NANJConfig.TX_RELAY_ADDRESS.replace("0x", "")}${NANJConfig.WALLET_OWNER.replace("0x", "")}$pad${NANJConfig.META_NANJCOIN_MANAGER.replace("0x", "")}${encodeFunction.replace("0x", "")}"
        val hash = Hash.sha3(hashInput)
        val restData = TxRelayData(
                Numeric.toHexString(sign.r),
                Numeric.toHexString(sign.s),
                sign.v.toString(),
                encodeFunction,
                nonceString,
                NANJConfig.META_NANJCOIN_MANAGER,//dest
                hash
        )
        val jsonParamApi = Gson().toJson(restData)
        val requestBody = RequestBody.create(MediaType.parse("application/json"), jsonParamApi)
        println("313123123131312313")
        NetworkUtil.retofit.create(Api::class.java)
                .postCreateNANJWallet(
                        NANJConfig.NANJ_SERVER_ADDRESS,
                        requestBody
                )
                .subscribe(
                        {
                            println("send success")
                            print("send success   " + Gson().toJson(it))
                            success.invoke()
                        },
                        {
                            println("send fail")
                            it.printStackTrace()
                            error.invoke()
                        }
                )
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
 