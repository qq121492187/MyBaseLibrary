package com.candidate.doupin.refactory.model.repository

import androidx.annotation.IntRange
import androidx.lifecycle.MutableLiveData
import com.example.mybaselibrary.mvvm.model.repository.BaseRepository
import com.example.mybaselibrary.net.resource.Resource

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
abstract class BasePageRepository<T> : BaseRepository() {

    /**
     * 状态管理
     */
    val repoStatus by lazy { MutableLiveData<Resource<Boolean>>() }

    /**
     * header数据管理
     */
    val headerRepo by lazy { MutableLiveData<Resource<Any>>() }

    /**
     *  初始化最好使用同步执行，避免刷新时列表闪烁。
     *  如果异步执行，列表会先清空，结果返回后刷新列表，
     *  导致列表出现闪烁现象
     */
    abstract fun pageInit(@IntRange(from = 0) initSize: Int, onPage: (list: List<T>, nextPage: Int) -> Unit)

    /**
     * 请求下一页
     */
    abstract fun getDataByPage(
            @IntRange(from = 0) page: Int, size: Int,
            onPage: (list: List<T>) -> Unit
    )

    /**
     * 处理页面数据的状态
     * @param res 要处理的数据状态
     * @param list 处理的数据集
     * @param isInit 是否为初始化的请求
     * @param onNext 状态正确无异常时的回调
     */
    protected fun handlePageStatus(
        res: Resource<Any>,
        list: List<T>? = null,
        isInit: Boolean = false,
        onNext: (list: List<T>) -> Unit
    ) {
        val status: Resource<Boolean>
        if (res is Resource.Success) {
            status = if (list.isNullOrEmpty()) {
                if (isInit) {
                    Resource.Success(false)
                } else {
                    Resource.Success(true)
                }
            } else {
                Resource.Success(true)
            }
        } else {
            status = Resource.Error(res.message, isInit)
        }
        repoStatus.postValue(status)
        if (status is Resource.Success && status.data!!) {
            onNext.invoke(list!!)
        }
    }

    protected fun handlePageStatus(
        res: Resource<Any>,
        isInit: Boolean = false,
        onNext: () -> Unit
    ) {
        val status: Resource<Boolean>
        if (res is Resource.Success) {
            status = Resource.Success(true)
        } else {
            status = Resource.Error(res.message, isInit)
        }
        repoStatus.postValue(status)
        if (status is Resource.Success && status.data!!) {
            onNext.invoke()
        }
    }

}