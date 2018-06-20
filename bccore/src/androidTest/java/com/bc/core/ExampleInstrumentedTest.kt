package com.bc.core

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.bc.core.model.Transaction
import com.bc.core.nanj.NANJConfig
import com.bc.core.nanj.NANJWallet
import com.bc.core.nanj.NANJWalletManager
import com.bc.core.nanj.listener.*

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    @Throws(Exception::class)
    fun createWallet() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val signal = CountDownLatch(1)
        nanjWalletManager?.createWallet("a", object : NANJCreateWalletListener {
            override fun onCreateProcess(backup: String, wallet: NANJWallet) {
                println("end success")
                signal.countDown()

            }

            override fun onCreateWalletFailure() {
                println("end fail")
                signal.countDown()

            }
        })



        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun importWalletFromPrivate() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val signal = CountDownLatch(1)
        nanjWalletManager?.importWallet("b", object  : NANJImportWalletListener {
            override fun onImportWalletSuccess() {
                signal.countDown()
            }

            override fun onImportWalletFailure() {
                signal.countDown()
            }
        })

        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun importWalletFromJsonKeystore() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val signal = CountDownLatch(1)
        val jsonKeystore = "{\"address\":\"b66e92f4713de200bc9cb61269a746aa005cbec3\",\"id\":\"722bb9c8-3912-464e-a8b4-2145c6d76934\",\"version\":3,\"crypto\":{\"cipher\":\"aes-128-ctr\",\"cipherparams\":{\"iv\":\"97de2fb0b4ecd5533368dd9cc82ee7b9\"},\"ciphertext\":\"a408ec0d18b80fae4c94f86f221b8e035c5381a181bff8b60a5d729af31625da\",\"kdf\":\"scrypt\",\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8,\"salt\":\"72e2e63d5e070a90f99dc11d71827d250a0896bff3d169ecf217dbbcd0ec6cd2\"},\"mac\":\"8396982f108b64bdf93a7bd6f5850ca3ba50c6047d8f9973b2c46d83eba6b61e\"}}"
        nanjWalletManager?.importWallet("qwerty2018", jsonKeystore,object  : NANJImportWalletListener {
            override fun onImportWalletSuccess() {
                signal.countDown()
            }

            override fun onImportWalletFailure() {
                signal.countDown()
            }
        })

        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun removeWallet() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        nanjWalletManager?.removeWallet(0)
    }

    @Test
    fun getNANJRate() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val signal = CountDownLatch(1)
        nanjWalletManager?.getNANJRate(object : NANJRateListener {
            override fun onSuccess(values: BigDecimal) {
                println("nanj rate ---- $values")
                assertEquals("a", "b")
                signal.countDown()
            }

            override fun onFailure(e: String) {
                println("nanj rate ---- failure")
                assertEquals("a", "b")
                signal.countDown()
            }
        })

        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun getNANJAddress() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val wallet = NANJWallet().apply {
            privatekey = "0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            cridentals = Credentials.create("0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
            init()
        }
        nanjWalletManager?.enableWallet(wallet)
//        val signal = CountDownLatch(1)
        nanjWalletManager?.getNANJWallet(wallet.address)
//        signal.await(60, TimeUnit.SECONDS)

    }

    @Test
    fun getTransactionList() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val wallet = NANJWallet().apply {
            privatekey = "0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            cridentals = Credentials.create("0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
            init()
        }
        nanjWalletManager?.enableWallet(wallet)
        val signal = CountDownLatch(1)
        nanjWalletManager?.wallet?.getTransactions(page = 0, listener = object : NANJTransactionsListener {
            override fun onTransferSuccess(transactions: MutableList<Transaction>?) {
                signal.countDown()
            }

            override fun onTransferFailure() {
                signal.countDown()
            }
        })
        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun sendNANJCoin() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val wallet = NANJWallet().apply {
            privatekey = "0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            cridentals = Credentials.create("0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
            init()
        }
        nanjWalletManager?.enableWallet(wallet)
        val signal = CountDownLatch(1)
        nanjWalletManager?.wallet?.sendNANJCoin("0xb66e92f4713de200bc9cb61269a746aa005cbec3", "1", object: SendNANJCoinListener{
            override fun onError() {
                signal.countDown()
            }

            override fun onSuccess() {
                signal.countDown()
            }
        })
        signal.await(60, TimeUnit.SECONDS)
    }

    @Test
    fun getNanjMount() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val nanjWalletManager = NANJWalletManager.Builder()
                .setContext(appContext)
                .build()
        val wallet = NANJWallet().apply {
            privatekey = "0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            cridentals = Credentials.create("0xd8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
            init()
        }
        nanjWalletManager?.enableWallet(wallet)
        nanjWalletManager?.wallet?.getAmountNanj()
    }

}
