package com.bc.core.nanj

import android.app.Activity
import android.support.v4.app.Fragment
import android.text.TextUtils
import com.bc.core.database.NANJDatabase
import com.bc.core.model.TransactionResponse
import com.bc.core.model.TxRelayData
import com.bc.core.nanj.NANJConfig.NANJWALLET_APP_ID
import com.bc.core.nanj.NANJConfig.NANJWALLET_SECRET_KEY
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
import org.web3j.abi.datatypes.generated.Bytes32
import org.web3j.abi.datatypes.generated.Uint256
import org.web3j.crypto.*
import org.web3j.protocol.Web3j
import org.web3j.utils.Numeric
import java.math.BigDecimal
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
    var privateKey: String? = null
    var credentials: Credentials? = null
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
                credentials!!
        )
    }

    private fun initTxRelay() {
        this.txRelay = TxRelay.load(
                NANJConfig.TX_RELAY_ADDRESS,
                _web3j,
                credentials!!
        )
    }

    private fun initMetaNANJCOINManager() {
        this.metaNANJCOINManager = MetaNANJCOINManager.load(
                web3j = _web3j,
                credentials = credentials!!
        )
    }

    fun getAmountNanj(): BigInteger {
        val nanjAddress = getNANJWallet()
        if (nanjAddress == NANJConfig.UNKNOWN_NANJ_WALLET) return BigInteger.ZERO
        println("wallet   ---- -  $nanjAddress")
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
                    NetworkUtil.retrofit.create(Api::class.java)
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
                { listener.onError() },
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
                    println("wallet credential1  fuck $address")
                    val param = arrayListOf<Type<*>>(Address(address))
                    val createNanjWalletFunction = Function("createWallet", param, arrayListOf())
                    sendFunctionToServer(
                            function = createNanjWalletFunction,
                            error = { listener?.onError() },
                            success = { listener?.onCreateProcess() }
                    )
                }
        )
    }

    fun sendNANJCoin(toAddress: String, amount: String, message: String = "", listener: SendNANJCoinListener? = null) {
        doAsync(
                {
                    it.printStackTrace()
                    listener?.onError()
                },
                {
                    val nanjAddress = metaNANJCOINManager!!.getWallet(address).send()
                    val nanjAmount = BigInteger(amount).multiply(BigInteger.TEN.pow(8))
                    val param = arrayListOf<Type<*>>(
                            Address(toAddress),
                            Uint256(nanjAmount),
                            DynamicBytes(message.toByteArray())
                    )
                    val f = Function("transfer", param, arrayListOf())
                    val data = FunctionEncoder.encode(f)
                    val hexData = Numeric.hexStringToByteArray(data)
                    val appHash = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470"//Hash.sha3String(NANJWALLET_APP_ID + NANJWALLET_SECRET_KEY)
                    val params = arrayListOf<Type<*>>(
                            Address(address),
                            Address(nanjAddress),
                            Address(NANJConfig.NANJCOIN_ADDRESS),
                            Uint256.DEFAULT,
                            DynamicBytes(hexData),
                            Bytes32(Numeric.hexStringToByteArray(appHash))
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

    @Suppress("RemoveSingleExpressionStringTemplate")
    private fun sendFunctionToServer(function: Function, error: () -> Unit, success: () -> Unit) {
        val encodeFunction = FunctionEncoder.encode(function)
//        val nanjAddress = metaNANJCOINManager!!.getWallet(address).send()
        val nonceString = txRelay!!.getNonce(address).send().toString(16)
        val pad = nonceString.padStart(64, '0')
        val hashInput = "0x1900" +
                "${NANJConfig.TX_RELAY_ADDRESS.cleanHexPrefix()}" +
                "${NANJConfig.WHITE_LIST_OWNER.cleanHexPrefix()}" +
                "$pad" +
                "${NANJConfig.META_NANJCOIN_MANAGER.cleanHexPrefix()}" +
                "${encodeFunction.cleanHexPrefix()}"
        val sign = Sign.signMessage(Numeric.hexStringToByteArray(hashInput), credentials!!.ecKeyPair)
        val hash = Hash.sha3(hashInput)
        val restData = TxRelayData(
                r = Numeric.toHexString(sign.r),
                s = Numeric.toHexString(sign.s),
                v = sign.v.toInt(),
                data = encodeFunction,
                nonce = nonceString,
                dest = NANJConfig.META_NANJCOIN_MANAGER,
                hash = hash
        )
        //{"data":"0xb054a9e8000000000000000000000000acb9808c16fe8a4e450b002a1857afb768d6d9b3","dest":"0x8801307aec8ed852ea23d3d4e69f475f4f2dcb6e","hash":"0x7664cc535641c649b82419e75419fe4197143affc006427404022a6f08c9dd6e","nonce":"0","r":"0xe122e5f31ebf41c694f9ae2f97cca4e04c5f2b85498cf70d19c9e09834d8a685","s":"0x3f5220107cd9aed210e6487967f7d29915c03a1a05b94dfd2480056f900bbdda","v":27}

        println("data to server $restData")
        val requestBody = RequestBody.create(MediaType.parse("application/json"), restData.toString())
        NetworkUtil.retrofit.create(Api::class.java)
                .postCreateNANJWallet(
                        NANJConfig.NANJ_SERVER_ADDRESS,
                        requestBody
                )
                .subscribe(
                        {
                            success.invoke()
                        },
                        {
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

inline fun String.cleanHexPrefix() = this.removePrefix("0x")
 