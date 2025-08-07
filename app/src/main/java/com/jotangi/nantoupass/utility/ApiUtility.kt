package com.jotangi.nantoupass.utility

import android.util.Log
import com.jotangi.nantoupass.config.ApiConfig
import okhttp3.OkHttpClient
import okhttp3.internal.http2.ConnectionShutdownException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

object ApiUtility {
    private var retrofit: Retrofit? = null
    private var retrofitCanton: Retrofit? = null
    private var retrofitTest: Retrofit? = null
    private var retrofitChanghua: Retrofit? = null

    private val clientChanghua: Retrofit
        get() {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.URL_HOST_Changhua)
//                .baseUrl(ApiConfig.TEST_URL_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit!!
        }

    val serviceChanghua = clientChanghua.create(ApiConfig::class.java)!!

    private val client: Retrofit
        get() {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(ApiConfig.URL_HOST)
//                .baseUrl(ApiConfig.TEST_URL_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit!!
        }

    val service = client.create(ApiConfig::class.java)!!

    private val clientCanton: Retrofit
        get() {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            retrofitCanton = Retrofit.Builder()
                .baseUrl(ApiConfig.URL_HOST_CANTON)
//                .baseUrl(ApiConfig.TEST_URL_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofitCanton!!
        }

    val serviceCanton = clientCanton.create(ApiConfig::class.java)!!

    private val clientTest: Retrofit
        get() {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()

            retrofitTest = Retrofit.Builder()
                .baseUrl(ApiConfig.REN_AI_URL_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofitTest!!
        }

    val serviceTest = clientTest.create(ApiConfig::class.java)!!

    fun apiFailureMessage(call: Any, t: Throwable): String {
        if (call is okhttp3.Call) {
            val code = t.message
            val url = call.request().url.toString()

            if (!code.isNullOrEmpty()) {
                Log.d("錯誤是：：：：：", code)
                Log.d("錯誤的 URL 是：：：：：", url)
            }
        }

        val message = when (t) {
            is ConnectionShutdownException -> "網路異常，請檢查連線設備"
            is SocketTimeoutException -> "網路連線逾時！"
            is IOException -> "網路連線失敗，請檢查網路！"
            else -> t.message!!
        }

        println("--------$message-----------")

        return message
    }
}