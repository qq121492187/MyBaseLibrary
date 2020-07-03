package com.example.mybaselibrary.net

import com.example.mybaselibrary.LibraryInit
import com.example.mybaselibrary.net.intercept.CommonParamsInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 *Create by lvhaoran
 *on 2019/9/20
 */
class RetrofitFactory {
    companion object {
        fun <T> createService(
            clz: Class<T>,
            baseUrl: String,
            interceptors: List<Interceptor> = emptyList()
        ): T {
            val retrofit = Retrofit.Builder().client(getOkHttpClient(interceptors))
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(clz)
        }

        private fun getOkHttpClient(interceptors: List<Interceptor>?): OkHttpClient {
            val builder = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(CommonParamsInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

            interceptors?.forEach { builder.addInterceptor(it) }

            return builder.build()
        }

        fun create() {

        }
    }

}