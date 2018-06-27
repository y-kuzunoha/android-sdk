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

    fun sendNANJCoin(toAddress: String, amount: String, listener: SendNANJCoinListener? = null) {
        doAsync(
                {
                    it.printStackTrace()
                    listener?.onError()
                },
                {
                    val nanjAddress = metaNANJCOINManager!!.getWallet(address).send()
                    val nanjAmount = BigInteger(amount).multiply(BigInteger.TEN.pow(8))
                    val feeAmount = nanjAmount.divide(BigInteger.TEN)
                    val param = arrayListOf<Type<*>>(
                            Address(toAddress),
                            Uint256(nanjAmount)
                    )
                    //0x73d61389000000000000000000000000e2e817342f159bb35de07d5e8babd72f0ce6c2a400000000000000000000000005ffea888ccea805e33a01f01462f770d27be951000000000000000000000000f7afb89bef39905ba47f3877e588815004f7c861000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e0c5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a47000000000000000000000000000000000000000000000000000000000000f42400000000000000000000000000000000000000000000000000000000000000044a9059cbb000000000000000000000000e2e817342f159bb35de07d5e8babd72f0ce6c2a40000000000000000000000000000000000000000000000000000000005f5e10000000000000000000000000000000000000000000000000000000000
                    //0x73d61389000000000000000000000000e2e817342f159bb35de07d5e8babd72f0ce6c2a400000000000000000000000005ffea888ccea805e33a01f01462f770d27be951000000000000000000000000f7afb89bef39905ba47f3877e588815004f7c861000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000e098dfdce47b7fa6edbffb738ead5e8210de4f5a2cb48e6517d1189c407176f5fa00000000000000000000000000000000000000000000000000000000009896800000000000000000000000000000000000000000000000000000000000000044a9059cbb00000000000000000000000005ffea888ccea805e33a01f01462f770d27be9510000000000000000000000000000000000000000000000000000000005f5e10000000000000000000000000000000000000000000000000000000000
                    val f = Function("transfer", param, arrayListOf())
                    val data = FunctionEncoder.encode(f)
                    val hexData = Numeric.hexStringToByteArray(data)
                    val appHash = Hash.sha3(NANJWALLET_APP_ID + NANJWALLET_SECRET_KEY)
                    val params = arrayListOf<Type<*>>(
                            Address(address),
                            Address(nanjAddress),
                            Address(NANJConfig.NANJCOIN_ADDRESS),
                            Uint256.DEFAULT,
                            DynamicBytes(hexData),
                            Bytes32(Numeric.hexStringToByteArray(appHash)),
                            Uint256(feeAmount)
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
        val hash = Hash.sha3(hashInput)
        val sign = Sign.signMessage(Numeric.hexStringToByteArray(hashInput), credentials!!.ecKeyPair)
        val restData = TxRelayData(
                r = Numeric.toHexString(sign.r),
                s = Numeric.toHexString(sign.s),
                v = sign.v.toInt(),
                data = encodeFunction,
                nonce = nonceString,
                dest = NANJConfig.META_NANJCOIN_MANAGER,
                hash = hash
        )
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
 