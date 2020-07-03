package com.example.mybaselibrary.mvvm.model.data

import androidx.paging.PageKeyedDataSource
import com.candidate.doupin.refactory.model.repository.BasePageRepository

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
class BasePageDataSource<T>(private val repo: BasePageRepository<T>) :
    PageKeyedDataSource<Int, T>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, T>
    ) {
        repo.pageInit(params.requestedLoadSize){list, nextPage ->
            callback.onResult(list, null, 1)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        repo.getDataByPage(params.key, params.requestedLoadSize) {
            callback.onResult(it, params.key + 1)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
        repo.getDataByPage(params.key, params.requestedLoadSize) {
            callback.onResult(it, params.key - 1)
        }
    }
}