package com.example.mybaselibrary.mvvm.model.repository

import com.example.mybaselibrary.mvvm.model.data.ReposDataProxy
import com.example.mybaselibrary.net.resource.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 *Create by lvhaoran
 *on 2019/9/20
 *
 * 需要根据业务自定义 reposData的成功值
 */
@ExperimentalCoroutinesApi
open class BaseRepository() :
    CoroutineScope by MainScope() {

    /**
     * 发起网络请求并处理得到后的结果
     * @param 请求得到的值
     * @return 处理后的数据
     */
    suspend fun <T> repos(b: suspend () -> ReposDataProxy<T>): Resource<T> {

        try {
            val reposData = b.invoke()
            if (reposData.getCode() == reposData.defineSuccessCode()) {
                return Resource.Success(
                    reposData.getData(), reposData.getCode(), reposData.getMsg()
                        ?: "success"
                )
            }
            return Resource.Error(reposData.getMsg(), code = reposData.getCode())
        } catch (e: Exception) {
            return Resource.Error(e.toString())
        }
    }

    /**
     * 清除所有请求
     */
    fun clear() {
        cancel()
    }
}