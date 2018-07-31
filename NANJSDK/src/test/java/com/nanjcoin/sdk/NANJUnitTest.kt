package com.nanjcoin.sdk

import com.nanjcoin.sdk.nanj.NANJConfig
import com.nanjcoin.sdk.nanj.NANJWallet
import com.nanjcoin.sdk.nanj.NANJWalletManager
import com.nanjcoin.sdk.nanj.listener.NANJCreateWalletListener
import com.nanjcoin.sdk.nanj.listener.NANJImportWalletListener
import com.nanjcoin.sdk.nanj.listener.NANJRateListener
import org.junit.Test
import org.junit.Assert.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import org.junit.BeforeClass
import org.mockito.MockitoAnnotations
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3jFactory
import org.web3j.protocol.http.HttpService
import java.math.BigDecimal


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */

class NANJUnitTest {

    companion object {
        @BeforeClass
        fun `init variable before run test`() {
            MockitoAnnotations.initMocks(this)
        }
    }

//    @Test
//    @Throws(Exception::class)
//    fun addition_isCorrect() {
//        assertEquals(4, (2 + 2).toLong())
//    }


    @Test
    @Throws(Exception::class)
    fun `create nanj wallet`() {
        val nanjWalletManager = NANJWalletManager.Builder().build()
        val signal = CountDownLatch(1)
        nanjWalletManager?.createWallet(object : NANJCreateWalletListener {
            override fun onCreateProcess(backup: String?) {
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
    fun `import wallet from private key`() {
        val nanjWalletManager = NANJWalletManager()
        val signal = CountDownLatch(1)
        nanjWalletManager.importWallet("b", object : NANJImportWalletListener {
            override fun onImportWalletSuccess() {
                signal.countDown()
            }

            override fun onImportWalletFailure() {
                signal.countDown()
            }
        })

        signal.await(60, TimeUnit.SECONDS)
    }

    fun `import wallet from json keystore`() {
        val nanjWalletManager = NANJWalletManager()
        val signal = CountDownLatch(1)
        nanjWalletManager.importWallet("b", "json keystore", object : NANJImportWalletListener {
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
    fun getNANJRate() {
        val nanjWalletManager = NANJWalletManager.Builder()
                .build()
        val signal = CountDownLatch(1)
        nanjWalletManager?.getNANJRate(object : NANJRateListener {
            override fun onSuccess(values: BigDecimal) {
                assertEquals("$values", "0.1588263664540200")
                signal.countDown()
            }

            override fun onFailure(e: String) {
                assertEquals("a", "b")
                signal.countDown()
            }
        })

        signal.await(60, TimeUnit.SECONDS)
    }


    @Test
    fun getNANJAddress() {
        val nanjWalletManager = NANJWalletManager.Builder()
                .build()
        val wallet = NANJWallet().apply {
            privateKey = "d8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            credentials = Credentials.create("d8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
        }
        nanjWalletManager?.enableWallet(wallet)
//        val signal = CountDownLatch(1)
        val a = nanjWalletManager?.getNANJWallet(wallet.address)
//        signal.await(60, TimeUnit.SECONDS)
        assertEquals(a, "0x00b74472f1b4c12a752bb6cfbf13955383e13634")
    }

}