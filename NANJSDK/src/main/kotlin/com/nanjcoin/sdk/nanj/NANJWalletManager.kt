package com.nanjcoin.sdk.nanj

import android.annotation.SuppressLint
import android.content.Context
import com.nanjcoin.sdk.nanj.listener.*
import com.nanjcoin.sdk.util.*
import com.fasterxml.jackson.databind.ObjectMapper
import com.nanjcoin.sdk.model.Erc20
import com.nanjcoin.sdk.model.NANJConfigModel
import com.nanjcoin.sdk.smartcontract.MetaNANJCOINManager
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.doAsync
import org.web3j.crypto.*
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File
import org.web3j.crypto.Wallet
import org.web3j.crypto.WalletFile
import java.lang.Exception

/**
 * ____________________________________
 *
 * Generator: NANJ Team - support@nanjcoin.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

open class NANJWalletManager {

    private var config: NANJConfigModel? = null

    private var wallets: MutableMap<String, NANJWallet> = mutableMapOf()
    private var _nanjDatabase: com.nanjcoin.sdk.database.NANJDatabase? = null
    private val _web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
    var wallet: NANJWallet? = null
        get() {
            if (field != null) {
                field!!.config = config
            }
            return field
        }
    private var metaNANJCOINManager: MetaNANJCOINManager? = null
    private var activeErc20Token: Erc20? = null
    var masterPassword = ""

    companion object {
        lateinit var instance: NANJWalletManager
    }

    class Builder {
        private var db: com.nanjcoin.sdk.database.NANJDatabase? = null
        fun setContext(context: Context): Builder {
            db = com.nanjcoin.sdk.database.NANJDatabase(context)
            return this
        }

        fun setDevelopmentMode(isDevelopment: Boolean): Builder {
            NANJConfig.setDevelopMode(isDevelopment)
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

    private fun loadNANJWallets() {
        wallets = _nanjDatabase?.loadWallets() ?: mutableMapOf()
        if (wallets.isNotEmpty()) {
            enableWallet(0)
        }
    }

    /**
     * Initialize NANJ SDK
     * @param listener, instance of NANJInitializationListener
     */
    fun initialize(listener: NANJInitializationListener){
        val manager = this
        NetworkUtil.retrofit.create(Api::class.java)
                .getNANJCoinConfig(NANJConfig.NANJ_SERVER_CONFIG)
                .subscribeOn(Schedulers.single())
                .observeOn(Schedulers.single())
                .subscribe(object : Observer<NANJConfigModel> {
                    override fun onSubscribe(d: Disposable) {
                        // Handle on subscribe
                    }

                    override fun onNext(responseBody: NANJConfigModel) {

                        if (responseBody.status == 200) {
                            try {
                                manager.setNANJConfig(responseBody, 0)
                                listener.onSuccess()
                            }
                            catch (exception: Exception) {
                                exception.printStackTrace()
                                listener.onError()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        listener.onError()
                    }

                    override fun onComplete() {}
                })
    }

    internal fun setNANJConfig(config: NANJConfigModel, erc20: Int) {
        this.config = config
        setSmartContract()
        setTxReplay()
        setErc20(erc20)
        setMetaNANJCOINManager()
        loadNANJWallets()
    }

    /**
     * set current Erc20 token
     * @param position of Erc20 token in Erc20 token list
     */
    fun setErc20(position: Int) {
        if (config?.data?.erc20s?.size ?: 0 > 0) {
            NANJConfig.SMART_CONTRACT_ADDRESS = config?.data?.erc20s?.get(position)?.address ?: ""
            this.activeErc20Token = config?.data?.erc20s?.get(position)

            wallet?.init()
        }
    }

    /**
     * get supported ERC20 tokens
     * @return list of Erc20
     */
    fun getErc20List() : List<Erc20> {
        if (config?.data?.erc20s?.size ?: 0 > 0) {
            return config?.data?.erc20s!!
        }
        return emptyList()
    }

    /**
     * Get current ERC20 Token which one is chosen
     * @return Erc20 Model
     */
    fun getCurrentErc20(): Erc20? {
        return this.activeErc20Token
    }


    private fun setMetaNANJCOINManager() {
        metaNANJCOINManager = MetaNANJCOINManager.load(
                web3j = _web3j,
                credentials = Credentials.create(Keys.createEcKeyPair()))
    }

    private fun setSmartContract() {
        NANJConfig.META_NANJCOIN_MANAGER = config?.data?.smartContracts?.get("metaNanjManager") ?: ""
    }

    private fun setTxReplay() {
        NANJConfig.TX_RELAY_ADDRESS = config?.data?.smartContracts?.get("txRelay") ?: ""
    }

    /**
     * get a wallet asynchronously
     * @param owner Eth address
     * @param listener GetNANJWalletListener
     */
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

    /**
     * get wallet with owner ETH address
     * @param owner is owner ETH address
     * @return NANJ Wallet address
     */
    fun getNANJWallet(owner: String) = metaNANJCOINManager!!.getWallet(owner).send()

    /**
     * get wallet list
     * @return list of wallets (NANJWallet List)
     */
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
                { nanjWalletListener.onImportWalletError() },
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
                                nanjWalletListener.onImportWalletError()
                            }

                            override fun onSuccess() {
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
                { nanjWalletListener.onImportWalletError() },
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
                                nanjWalletListener.onImportWalletError()
                            }

                            override fun onSuccess() {
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
                { nanjWalletListener.onImportWalletError() },
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
                                nanjWalletListener.onImportWalletError()
                            }

                            override fun onSuccess() {
                                nanjWalletListener.onImportWalletSuccess()
                            }
                        })

                    }
                }
        )

    }

    private fun importWalletFromCredentials(credentials: Credentials, password: String, nanjAddress: String? = ""): NANJWallet {
        val nanjWallet = NANJWallet().apply {
            this.address = credentials.address
            this.credentials = credentials
            this.privateKey = credentials.ecKeyPair.privateKey.toString(16)
            this.nanjAddress = nanjAddress
            this.keystore = convertPrivateKeyToKeystore(credentials.ecKeyPair.privateKey.toString(16), password)
        }
        wallets[nanjWallet.address] = nanjWallet
        _nanjDatabase?.saveWallet(nanjWallet)
//        enableWallet(nanjWallet)
        return nanjWallet
    }

    /**
     * remove a wallet with its position
     * @param position of wallet
     */
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

    /**
     * remove a wallet from stored wallets
     * @param wallet to be removed
     */
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
                    createWalletListener.onWalletCreationError()
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
                            createWalletListener.onWalletCreationError()
                        }

                        override fun onSuccess() {
                            val wallet = importWalletFromCredentials(credentials, "")
                            createWalletListener.onCreatedWalletSuccess(wallet.privateKey)
                        }
                    })


                }
        )
    }

    /**
     * convert private key to keystore with master password
     * @param privateKey string
     * @return keystore
     */
    fun convertPrivateKeyToKeystore(privateKey: String): String {
        return this.convertPrivateKeyToKeystore(privateKey,masterPassword)
    }

    /**
     * convert private key to key store with password
     * @param privateKey string
     * @param password string
     * @return keystore
     */
    fun convertPrivateKeyToKeystore(privateKey: String, password: String): String {
        val credentials = Credentials.create(privateKey)
        val ecKeyPair = credentials.ecKeyPair
        val aWallet = Wallet.createLight(password, ecKeyPair)
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(aWallet)
    }


    /**
     * enable a wallet
     * @param wallet to be enabled
     */
    fun enableWallet(wallet: NANJWallet) {
        wallet.privateKey = convertKeystoreToPrivateKey(wallet.keystore!!, masterPassword)
        val c = if (wallet.credentials == null) Credentials.create(wallet.privateKey) else wallet.credentials
        this.wallet = wallet.apply {
            this.web3j = _web3j
            this.credentials = c
            nanjDatabase = _nanjDatabase
        }
        this.wallet!!.init()
    }

    /**
     * enable a wallet at position in wallet list
     * @param position of wallet
     */
    fun enableWallet(position: Int) {
        if (position < 0 || position >= wallets.size) return // return if out size of list
        val list = wallets.keys.toMutableList()
        val key = list[position]
        val wallet = wallets[key]!!
        wallet.privateKey = convertKeystoreToPrivateKey(wallet.keystore!!, masterPassword)
        if (!wallet.nanjAddress.isNullOrBlank()) {
            this.enableWallet(wallet)
        }
    }

    /**
     * Get NANJCOIN/JPY rate
     * @param NANJRateListener
     */
    @SuppressLint("CheckResult")
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

    /**
     * Get NANJCOIN vs USD or JPY rate
     * @param coinName Name of ERC20 token
     * @param currencySymbol 'usd' or 'jpy'
     * @param listener NANJRateListener
     */
    @SuppressLint("CheckResult")
    fun getNANJGetRate(coinName : String, currencySymbol : String, listener: NANJRateListener) {
        val url = String.format(
                NANJConfig.URL_GET_RATE,
                coinName,
                currencySymbol
        )

        NetworkUtil.retrofit.create(Api::class.java)
                .getNANJGetRate(url)
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

    private fun convertKeystoreToPrivateKey(jsonKeystore: String, password: String): String {
        val objectMapper = ObjectMapper()
        val walletFile = objectMapper.readValue(jsonKeystore, WalletFile::class.java)
        return Wallet.decrypt(password, walletFile).privateKey.toString(16)
    }

}