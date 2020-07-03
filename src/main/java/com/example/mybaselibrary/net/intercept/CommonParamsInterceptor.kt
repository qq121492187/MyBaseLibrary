package com.example.mybaselibrary.net.intercept

import okhttp3.*

/**
 *Create by lvhaoran
 *on 2019/8/2
 */
class CommonParamsInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        //添加请求头
        val builder = request.newBuilder()
                .addHeader("V", "Version")
        //GET
        if (request.method() == "GET") {
            val httpUrl = request.url()
                    .newBuilder()
                    .addQueryParameter("timestamp", System.currentTimeMillis().toString()) //添加公共参数
                    .build()
            builder.url(httpUrl)
        }

        //POST
        if (request.method() == "POST") {
            if (request.body() is FormBody) {
                val bodyBuilder = FormBody.Builder()
                var formBody = request.body() as FormBody
                //将参数添加至新的构造器
                for (i in 0 until formBody.size()) {
                    bodyBuilder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i))
                }

                formBody = bodyBuilder
                        .addEncoded("timestamp", System.currentTimeMillis().toString()) //添加公共参数
                        .build()
                builder.post(formBody)
            }
        }
        return chain.proceed(builder.build())
    }
}