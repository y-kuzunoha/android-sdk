package com.bc.core

import com.bc.core.nanj.NANJConfig
import com.bc.core.nanj.NANJWallet
import com.bc.core.nanj.NANJWalletManager
import com.bc.core.nanj.listener.NANJCreateWalletListener
import com.bc.core.nanj.listener.NANJImportWalletListener
import com.bc.core.nanj.listener.NANJRateListener
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

class ExampleUnitTest {

   companion object {
       @BeforeClass
       fun `init variable before run test` () {
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
    fun `import wallet from private key`() {
        val nanjWalletManager = NANJWalletManager()
        val signal = CountDownLatch(1)
        nanjWalletManager.importWallet("b", object  : NANJImportWalletListener {
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
        nanjWalletManager.importWallet("b", "json keystore",object  : NANJImportWalletListener {
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
                println("nanj rate ---- $values")
                assertEquals("$values", "0.1588263664540200")
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
        val nanjWalletManager = NANJWalletManager.Builder()
                .build()
        val wallet = NANJWallet().apply {
            privatekey = "d8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020"
            address = "0xb66e92f4713de200bc9cb61269a746aa005cbec3"
            cridentals = Credentials.create("d8816e6d65b327575cdfe58dbe3ed83ade7079dc4885ef51cf38e795a6d71020")
            web3j = Web3jFactory.build(HttpService(NANJConfig.URL_SERVER))
        }
        nanjWalletManager?.enableWallet(wallet)
//        val signal = CountDownLatch(1)
        val a = nanjWalletManager?.getNANJWallet(wallet.address)
//        signal.await(60, TimeUnit.SECONDS)
        assertEquals(a, "0x00b74472f1b4c12a752bb6cfbf13955383e13634")
    }

}