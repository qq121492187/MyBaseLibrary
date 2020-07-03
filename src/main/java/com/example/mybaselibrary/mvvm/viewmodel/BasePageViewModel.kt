package com.example.mybaselibrary.mvvm.viewmodel

import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.candidate.doupin.refactory.model.repository.BasePageRepository
import com.example.mybaselibrary.mvvm.model.data.BasePageDataSourceFactory

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
class BasePageViewModel<T>(
    private val repo: BasePageRepository<T>,
    private val config: PagedList.Config? = null
) : BaseViewModel(repo) {

    private val dataSourceFactory by lazy { BasePageDataSourceFactory(repo) }
    private val pageConfig by lazy {
        config ?: PagedList.Config.Builder()
            .setInitialLoadSizeHint(10) //初始加载数量
            .setPageSize(10)    //每页数量
            // .setMaxSize(50)   //最大数量
            .setPrefetchDistance(3) //距离底部多少个Item时开始加载下一页
            .setEnablePlaceholders(true) //是否使用null占位符
            .build()
    }

    val pageData = LivePagedListBuilder(dataSourceFactory, pageConfig)
        .setBoundaryCallback(object : PagedList.BoundaryCallback<T>() {
            override fun onZeroItemsLoaded() {
                super.onZeroItemsLoaded()
            }

            override fun onItemAtEndLoaded(itemAtEnd: T) {
                super.onItemAtEndLoaded(itemAtEnd)
            }
        })
        .build()

    //请求状态
    val repoStatus = repo.repoStatus
    //header 数据
    val headerRepo = repo.headerRepo

    fun refresh() {
        dataSourceFactory.dataSource?.invalidate()
    }
}