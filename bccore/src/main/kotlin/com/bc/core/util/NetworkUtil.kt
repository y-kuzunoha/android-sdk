package com.bc.core.util

import com.bc.core.nanj.NANJConfig
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtil {
    fun GET(urlApi: String, listener : (String) -> Unit) {
        val url = URL(urlApi)
        val httpURLConnection = url.openConnection() as HttpURLConnection
        val stringBuilder = StringBuilder()
        try {
            val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                stringBuilder.append(line).append("\n")
                line = bufferedReader.readLine()
            }
            bufferedReader.close()
        } finally {
            httpURLConnection.disconnect()
        }
        listener.invoke(stringBuilder.toString())
    }


    val retofit : Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(NANJConfig.NANJ_SERVER_ADDRESS)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}
interface Api {
    @GET fun getNANJRate(@Url url : String) : io.reactivex.Observable<ResponseBody>
    @GET fun getYenRate(@Url url : String) : io.reactivex.Observable<ResponseBody>
    @POST fun postCreateNANJWallet(@Url url : String, @Body txRelay: RequestBody) : io.reactivex.Observable<ResponseBody>
    @GET fun getNANJTransactions(@Url url : String) :  io.reactivex.Observable<ResponseBody>
}

