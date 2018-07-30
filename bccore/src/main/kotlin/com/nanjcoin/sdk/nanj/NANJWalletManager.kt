package com.nanjcoin.sdk.nanj

import android.content.Context
import com.nanjcoin.sdk.model.NANJRateData
import com.nanjcoin.sdk.model.YenRate
import com.nanjcoin.sdk.nanj.listener.*
import com.nanjcoin.sdk.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import com.nanjcoin.sdk.smartcontract.MetaNANJCOINManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.web3j.crypto.*
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile


/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

open class NANJWalletManager {

    companion object {
        lateinit var instance: NANJWalletManager
    }

    class Builder {
        private var db: com.nanjcoin.sdk.database.NANJDatabase? = null
        fun setContext(context: Context): Builder {
            db = com.nanjcoin.sdk.database.NANJDatabase(context)
            return this
        }

        fun setNANJAppId(appId: String): Builder {
            NANJConfig.NANJWALLET_APP_ID = appId
            return this
        }

        fun setNANJSecret(secret: String): Builder {
            NANJConfig.NANJWALLET_SECRET_KEY = secret
            return this
        }

        fun build(): NANJWalletManager? {
            instance = NANJWalletManager().apply {
                _nanjDatabase = db
            }
            return instance
        }
    }

    fun loadNANJWallets() {
        wallets = _nanjDatabase?.loadWallets() ?: mutableMapOf()
        if (wallets.isNotEmpty()) {
            enableWallet(0)
        }
    }

    var config: com.nanjcoin.sdk.model.NANJConfigModel? = null
    private var wallets: MutableMap<String, NANJWallet> = mutableMapOf()
    private var _nanjDatabase: com.nanjcoin.sdk.database.NANJDatabase? = null
    private val _web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER).apply {

    })
    var wallet: NANJWallet? = null
        get() {
            if (field != null) {
                field!!.config = config
            }
            return field
        }
    private var metaNANJCOINManager: MetaNANJCOINManager? = null

    fun setErc20(position: Int) {
        if (config?.data?.erc20s?.size ?: 0 > 0) {
            NANJConfig.SMART_CONTRACT_ADDRESS = config?.data?.erc20s?.get(position)?.address ?: ""
            NANJConfig.NANJWALLET_NAME = config?.data?.erc20s?.get(position)?.name ?: ""
            wallet?.init()
        }
    }

    fun setMetaNANJCOINManager() {
        metaNANJCOINManager = MetaNANJCOINManager.load(
                web3j = _web3j,
                credentials = Credentials.create(Keys.createEcKeyPair()))
    }

    fun setSmartContract() {
        NANJConfig.META_NANJCOIN_MANAGER = config?.data?.smartContracts?.get("metaNanjManager") ?: ""
    }

    fun setTxReplay() {
        NANJConfig.TX_RELAY_ADDRESS = config?.data?.smartContracts?.get("txRelay") ?: ""
    }

    fun getNANJWalletAsync(owner: String, listener: GetNANJWalletListener) {
        doAsync(
                { listener.onError() },
                {
                    val address = metaNANJCOINManager!!.getWallet(owner).send()
                    if (NANJConfig.UNKNOWN_NANJ_WALLET != address) {
                        _nanjDatabase?.updateWallet(owner, address)
                    }
                    listener.onSuccess(address)
                }
        )
    }

    fun getNANJWallet(owner: String) = metaNANJCOINManager!!.getWallet(owner).send()

    fun getWalletList(): MutableList<NANJWallet> = wallets.values.toMutableList()

    /**
     * Import a wallet from keystore
     *
     * @param password
     * @param fileKeystore
     *
     * */
    fun importWallet(password: String, fileKeystore: File, nanjWalletListener: NANJImportWalletListener) {
        doAsync(
                { nanjWalletListener.onImportWalletFailure() },
                {
                    val credentials = WalletUtils.loadCredentials(password, fileKeystore)
                    metaNANJCOINManager = MetaNANJCOINManager.load(
                            web3j = _web3j,
                            credentials = credentials
                    )
                    importWalletFromCredentials(credentials, "")
                    val nanjAddress = metaNANJCOINManager!!.getWallet(credentials.address).send()
                    if (NANJConfig.UNKNOWN_NANJ_WALLET != nanjAddress) {
                        importWalletFromCredentials(credentials, nanjAddress)
                        nanjWalletListener.onImportWalletSuccess()
                    } else {

                        this@NANJWalletManager.wallet?.createNANJWallet(object : CreateNANJWalletListener {
                            override fun onError() {
                                nanjWalletListener.onImportWalletFailure()
                            }

                            override fun onCreateProcess() {
                                nanjWalletListener.onImportWalletSuccess()
                            }
                        })

                    }
                }
        )
    }

    /**
     * Import a wallet from json
     *
     * @param password
     * @param jsonKeystore
     *
     * */
    fun importWallet(password: String, jsonKeystore: String, nanjWalletListener: NANJImportWalletListener) {
        doAsync(
                { nanjWalletListener.onImportWalletFailure() },
                {
                    val objectMapper = ObjectMapper()
                    val walletFile = objectMapper.readValue(jsonKeystore, WalletFile::class.java)
                    val credentials = Credentials.create(Wallet.decrypt(password, walletFile))
                    metaNANJCOINManager = MetaNANJCOINManager.load(
                            web3j = _web3j,
                            credentials = credentials
                    )
                    importWalletFromCredentials(credentials, "")
                    val nanjAddress = metaNANJCOINManager!!.getWallet(credentials.address).send()
                    if (NANJConfig.UNKNOWN_NANJ_WALLET != nanjAddress) {
                        importWalletFromCredentials(credentials, nanjAddress)
                        nanjWalletListener.onImportWalletSuccess()
                    } else {

                        this@NANJWalletManager.wallet?.createNANJWallet(object : CreateNANJWalletListener {
                            override fun onError() {
                                nanjWalletListener.onImportWalletFailure()
                            }

                            override fun onCreateProcess() {
                                nanjWalletListener.onImportWalletSuccess()
                            }
                        })

                    }
                }
        )
    }

    /**
     * Import a wallet from private key
     *
     * @param privateKey
     *
     * */
    fun importWallet(privateKey: String, nanjWalletListener: NANJImportWalletListener) {
        doAsync(
                { nanjWalletListener.onImportWalletFailure() },
                {
                    val credentials = Credentials.create(privateKey)
                    metaNANJCOINManager = MetaNANJCOINManager.load(
                            web3j = _web3j,
                            credentials = credentials
                    )
                    importWalletFromCredentials(credentials, "")
                    val nanjAddress = metaNANJCOINManager!!.getWallet(credentials.address).send()
                    if (NANJConfig.UNKNOWN_NANJ_WALLET != nanjAddress) {
                        importWalletFromCredentials(credentials, nanjAddress)
                        nanjWalletListener.onImportWalletSuccess()
                    } else {

                        this@NANJWalletManager.wallet?.createNANJWallet(object : CreateNANJWalletListener {
                            override fun onError() {
                                nanjWalletListener.onImportWalletFailure()
                            }

                            override fun onCreateProcess() {
                                nanjWalletListener.onImportWalletSuccess()
                            }
                        })

                    }
                }
        )

    }

    private fun importWalletFromCredentials(credentials: Credentials, nanjAddress: String? = ""): NANJWallet {
        val nanjWallet = NANJWallet().apply {
            this.address = credentials.address
            this.credentials = credentials
            this.privateKey = credentials.ecKeyPair.privateKey.toString(16)
            this.nanjAddress = nanjAddress
        }
        wallets[nanjWallet.address] = nanjWallet
        _nanjDatabase?.saveWallet(nanjWallet)
//        enableWallet(nanjWallet)
        return nanjWallet
    }

    fun removeWallet(position: Int) {
        if (position >= wallets.keys.toMutableList().size) return
        val key = wallets.keys.toMutableList()[position]
        wallets.remove(key)
        this.wallet?.let {
            if (it.address == key) {
                this.wallet = null
            }
        }
    }

    fun removeWallet(wallet: NANJWallet) {
        _nanjDatabase?.removeWallet(wallet)
        wallets.remove(wallet.address)
        this.wallet?.let {
            if (it.address == wallet.address) {
                this.wallet = null
            }
        }
    }

    /**
     * Create a wallet.
     *
     * */
    fun createWallet(createWalletListener: NANJCreateWalletListener) {
        doAsync(
                {
                    it.printStackTrace()
                    createWalletListener.onCreateWalletFailure()
                },
                {
                    val ecKeyPair = Keys.createEcKeyPair()
                    val credentials = Credentials.create(ecKeyPair)

                    val nanjWalletTmp = NANJWallet().apply {
                        this.address = credentials.address
                        this.credentials = credentials
                        this.privateKey = credentials.ecKeyPair.privateKey.toString(16)
                        this.nanjAddress = ""
                        this.web3j = _web3j
                        init()
                    }
                    nanjWalletTmp.createNANJWallet(object : CreateNANJWalletListener {
                        override fun onError() {
                            createWalletListener.onCreateWalletFailure()
                        }

                        override fun onCreateProcess() {
                            val wallet = importWalletFromCredentials(credentials, "")
                            createWalletListener.onCreateProcess(wallet.privateKey)
                        }
                    })


                }
        )
    }

    fun convertPrivateKeyToKeystore(privateKey: String, password: String): String {
        val credentials = Credentials.create(privateKey)
        val ecKeyPair = credentials.ecKeyPair
        val aWallet = Wallet.createLight(password, ecKeyPair)
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(aWallet)
    }

    fun exportPrivateKey(): String? = wallet?.privateKey ?: null
    fun exportKeystore(password: String): String? {
        return if (wallet?.privateKey.isNullOrBlank()) {
            null
        } else {
            convertPrivateKeyToKeystore(password, wallet!!.privateKey!!)
        }
    }


    fun enableWallet(wallet: NANJWallet) {
        val c = if (wallet.credentials == null) Credentials.create(wallet.privateKey) else wallet.credentials
        this.wallet = wallet.apply {
            this.web3j = _web3j
            this.credentials = c
            nanjDatabase = _nanjDatabase
        }
        this.wallet!!.init()
    }

    fun enableWallet(position: Int) {
        if (position < 0 || position >= wallets.size) return // return if out size of list
        val list = wallets.keys.toMutableList()
        val key = list[position]
        val wallet = wallets[key]!!
        if (!wallet.nanjAddress.isNullOrBlank()) {
            this.enableWallet(wallet)
        }
    }

    fun getNANJRate(listener: NANJRateListener) {
        NetworkUtil.retrofit.create(Api::class.java)
                .getNANJRate(NANJConfig.URL_YEN_RATE)
                .subscribeOn(Schedulers.single())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            listener.onSuccess(it.data.currentPrice)
                        },
                        {
                            it.printStackTrace()
                            listener.onFailure("error")
                        }
                )
    }

}