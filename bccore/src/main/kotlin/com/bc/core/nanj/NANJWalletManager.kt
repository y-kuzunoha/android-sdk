package com.bc.core.nanj

import android.content.Context
import com.bc.core.database.NANJDatabase
import com.bc.core.model.NANJRateData
import com.bc.core.model.YenRate
import com.bc.core.util.NANJCOIN_ADDRESS
import com.bc.core.util.NetworkUtil
import com.bc.core.util.TX_RELAY_ADDRESS
import com.bc.core.util.uiThread
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.web3j.crypto.*
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.io.File
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * ____________________________________
 *
 * Generator: Hieu.TV - tvhieuit@gmail.com
 * CreatedAt: 4/15/18
 * ____________________________________
 */

class NANJWalletManager constructor(context: Context) {

    private var wallets: MutableMap<String, NANJWallet> = mutableMapOf()
    private val _nanjDatabase = NANJDatabase(context)
    private val _web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
    var wallet: NANJWallet? = null

    init {
        wallets = _nanjDatabase.loadWallets()
        if (wallets.isNotEmpty()) {
            enableWallet(0)
        }
    }

    fun addWallet(wallet: NANJWallet) {
        wallets[wallet.address] = wallet
    }

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
                { uiThread { nanjWalletListener.onImportWalletFailure() } },
                {
                    val credentials = WalletUtils.loadCredentials(password, fileKeystore)
                    val wallet: WalletFile = Wallet.createLight(password, credentials.ecKeyPair)
                    val objectMapper = ObjectMapper()
                    importWalletFromCredentials(credentials, objectMapper.writeValueAsString(wallet))
                    uiThread { nanjWalletListener.onImportWalletSuccess() }
                }
        )
    }

    /**
     * Import a wallet from json
     *
     * @param password
     * @param jsonWallet
     *
     * */
    fun importWallet(password: String, jsonWallet: String, nanjWalletListener: NANJImportWalletListener) {
        doAsync(
                { uiThread { nanjWalletListener.onImportWalletFailure() } },
                {
                    val objectMapper = ObjectMapper()
                    val walletFile = objectMapper.readValue(jsonWallet, WalletFile::class.java)
                    val credentials = Credentials.create(Wallet.decrypt(password, walletFile))
                    importWalletFromCredentials(credentials, jsonWallet)
                    uiThread { nanjWalletListener.onImportWalletSuccess() }
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
                { uiThread { nanjWalletListener.onImportWalletFailure() } },
                {
                    val credential = Credentials.create(privateKey)
                    importWalletFromCredentials(credential)
                    uiThread { nanjWalletListener.onImportWalletSuccess() }
                }
        )

    }

    private fun importWalletFromCredentials(credentials: Credentials, keystore: String? = null): NANJWallet {
        val nanjWallet = NANJWallet().apply {
            this.address = credentials.address
            this.cridentals = credentials
            this.privatekey = credentials.ecKeyPair.privateKey.toString(16)
        }
        wallets[nanjWallet.address] = nanjWallet
        _nanjDatabase.saveWallet(nanjWallet, keystore)
        enableWallet(nanjWallet)
        return nanjWallet
    }

    fun removeWallet(position: Int) {
        val key = wallets.keys.toMutableList()[position]
        wallets.remove(key)
        this.wallet?.let {
            if (it.address == key) {
                this.wallet = null
            }
        }
    }

    fun removeWallet(wallet: NANJWallet) {
        _nanjDatabase.removeWallet(wallet)
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
     * @param password
     *
     * */
    fun createWallet(password: String, createWalletListener: NANJCreateWalletListener) {
        doAsync(
                { uiThread { createWalletListener.onCreateWalletFailure() } },
                {
                    val ecKeyPair = Keys.createEcKeyPair()
                    val addressWallet = Wallet.createLight(password, ecKeyPair)
                    val credentials = Credentials.create(Wallet.decrypt(password, addressWallet))
                    val objectMapper = ObjectMapper()
                    val strWallet = objectMapper.writeValueAsString(addressWallet)
                    val wallet = importWalletFromCredentials(credentials, strWallet)
                    uiThread { createWalletListener.onCreateWalletSuccess(strWallet, wallet) }
                }
        )
    }

    fun enableWallet(wallet: NANJWallet) {
        val c = Credentials.create(wallet.privatekey)
        this.wallet = wallet.apply {
            this.web3j = _web3j
            this.cridentals = c
            init()
        }
    }

    fun enableWallet(position: Int) {
        if (position < 0 || position >= wallets.size) return // return if out size of list
        val list = wallets.keys.toMutableList()
        val key = list[position]
        val wallet = wallets[key]!!
        this.enableWallet(wallet)
    }

    fun getNANJRate(listener: NANJRateListener) {
        doAsync(
                {
                    it.printStackTrace()
                    uiThread { listener.onFailure("error") }
                },
                {
                    var nanjRate: BigDecimal = 0.toBigDecimal()
                    var yenRate: BigDecimal = 0.toBigDecimal()
                    NetworkUtil.GET(NANJConfig.URL_NANJ_RATE) {
                        val nanjRateData = Gson().fromJson(it, NANJRateData::class.java)
                        val quotes = nanjRateData.data.quotes
                        nanjRate = quotes.USD.price
                                .divide(quotes.NANJ.price, 10 , RoundingMode.HALF_UP)
                    }

                    NetworkUtil.GET(NANJConfig.URL_YEN_RATE) {
                        val yenData = Gson().fromJson(it, YenRate::class.java)
                        yenRate = yenData.Usd2Yen.value
                    }

                    uiThread { listener.onSuccess(nanjRate.multiply(yenRate)) }
                }
        )
    }

}