package com.bc.core

import android.os.Looper
import android.support.annotation.NonNull
import com.bc.core.nanj.NANJWallet
import com.bc.core.nanj.NANJWalletManager
import com.bc.core.nanj.listener.CreateNANJWalletListener
import com.bc.core.nanj.listener.NANJCreateWalletListener
import com.bc.core.nanj.listener.NANJImportWalletListener
import io.reactivex.Scheduler
import org.junit.Test

import org.junit.Assert.*
import java.util.*
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.plugins.RxJavaPlugins.setInitSingleSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitNewThreadSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitComputationSchedulerHandler
import io.reactivex.plugins.RxJavaPlugins.setInitIoSchedulerHandler
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Executor
import org.mockito.internal.util.reflection.Whitebox




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
}