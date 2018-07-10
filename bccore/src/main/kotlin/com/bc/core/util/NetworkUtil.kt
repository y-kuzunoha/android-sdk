package com.bc.core.util

import com.bc.core.model.NANJConfigModel
import com.bc.core.nanj.NANJConfig
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

object NetworkUtil {
    fun GET(urlApi: String, listener: (String) -> Unit) {
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

    @JvmStatic private val okHttp : OkHttpClient by lazy {
        val httpClient = OkHttpClient.Builder()
        httpClient.addNetworkInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.HEADERS
                }
        )

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                    .header("Client-ID",  NANJConfig.NANJWALLET_APP_ID)
                    .header("Secret-Key",  NANJConfig.NANJWALLET_SECRET_KEY)

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        httpClient.build()
    }


    @JvmStatic
    open val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(NANJConfig.NANJ_SERVER_ADDRESS + "/")
                .client(okHttp)
                .client(
                        OkHttpClient.Builder().addInterceptor(
                                HttpLoggingInterceptor().apply {
                                    level = HttpLoggingInterceptor.Level.BODY
                                }
                        ).build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}

interface Api {
    @GET
    fun getNANJRate(@Url url: String): io.reactivex.Observable<ResponseBody>

    @GET
    fun getYenRate(@Url url: String): io.reactivex.Observable<ResponseBody>

    @POST
    fun postCreateNANJWallet(@Url url: String, @Body txRelay: RequestBody): io.reactivex.Observable<ResponseBody>

    @GET
    fun getNANJTransactions(@Url url: String): io.reactivex.Observable<ResponseBody>

    @POST
    fun getNANJCoinConfig(@Url url: String,
                          @Header("Client-ID") clientId: String,
                          @Header("Secret-Key") secretKey: String
    ): io.reactivex.Observable<NANJConfigModel>
}

