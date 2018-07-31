package com.nanjcoin.sdk.util

import com.nanjcoin.sdk.model.NANJNonce
import com.nanjcoin.sdk.model.RateResponse
import com.nanjcoin.sdk.nanj.NANJConfig
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

object NetworkUtil {

    @JvmStatic
    private val okHttp: OkHttpClient by lazy {
        OkHttpClient.Builder().apply {
            addNetworkInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    }
            )
            addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                        .addHeader("Client-ID", NANJConfig.NANJWALLET_APP_ID)
                        .addHeader("Secret-Key", NANJConfig.NANJWALLET_SECRET_KEY)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
        }.build()
    }


    @JvmStatic
    open val retrofit: Retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(NANJConfig.NANJCOIN_URL + "/")
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }
}

interface Api {
    @POST
    fun postCreateNANJWallet(@Url url: String, @Body txRelay: RequestBody): io.reactivex.Observable<ResponseBody>

    @GET
    fun getNANJTransactions(@Url url: String): io.reactivex.Observable<ResponseBody>

    @POST
    fun getNANJCoinConfig(@Url url: String): io.reactivex.Observable<com.nanjcoin.sdk.model.NANJConfigModel>

    @GET
    fun getNANJRate(@Url url: String) : io.reactivex.Observable<RateResponse>

    @GET
    fun getNANJNonce(@Url url: String) : io.reactivex.Observable<NANJNonce>
}

