package com.example.mybaselibrary.mvvm.ui

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.candidate.doupin.refactory.model.repository.BasePageRepository
import com.example.mybaselibrary.R
import com.example.mybaselibrary.adapter.BasePageAdapter
import com.example.mybaselibrary.extentions.dp2px
import com.example.mybaselibrary.extentions.getResColor
import com.example.mybaselibrary.mvvm.viewmodel.BasePageViewModel
import com.example.mybaselibrary.mvvm.viewmodel.DataBindingConfig
import com.example.mybaselibrary.utils.LibraryInjectorUtil
import com.example.mybaselibrary.views.DefaultItemDecoration
import com.example.mybaselibrary.net.resource.Resource
import kotlinx.android.synthetic.main.fragment_base_page.*

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
abstract class BasePageFragment<T> : LibraryFragment<T>() {

    protected lateinit var mAdapter: BasePageAdapter<T>
    protected val mStatusView by lazy { statusRoot }

    protected val viewModel: BasePageViewModel<T> by viewModels {
        providePageFactory()
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_base_page)
    }


    override fun initializeUI() {
        mAdapter = bindAdapter()
        mAdapter.onItemClick { data, position -> onItemClick(data, position) }
        initRecyclerView()
        refresh.setEnableRefresh(enabledRefresh())
        refresh.setOnRefreshListener {
            doRefresh()
        }
    }

    open fun initRecyclerView() {
        rv.adapter = mAdapter
        rv.addItemDecoration(getItemDecoration())
    }

    open fun getItemDecoration(): RecyclerView.ItemDecoration {
        return DefaultItemDecoration(
            requireContext().getResColor(R.color.divider_color),
            0,
            1.dp2px(),
            mAdapter.HeadType
        )
    }

    private fun observableUI() {
        viewModel.repoStatus.observe(this, Observer {
            subscribeStatus(it)
        })
        viewModel.pageData.observe(this, Observer {
            subscribeUI(it)
        })
        viewModel.headerRepo.observe(this, Observer { res ->
            handleResource(res, loading = {}, action = { subscribeHeader(it) })
        })
    }

    open fun subscribeUI(list: PagedList<T>) {
        refresh.finishRefresh()
        mAdapter.submitList(list)
        mAdapter.notifyDataSetChanged()
    }

    private fun subscribeHeader(headerData: Any?) {
        if (mAdapter.needHeader() && headerData != null) {
            mAdapter.setHeaderData(headerData)
        }
    }

    private fun subscribeStatus(status: Resource<Boolean>) {
        when (status) {
            is Resource.Success -> {
                refresh.finishRefresh()
                if (status.data!!) {
                    //加载成功
                    onPageLoaded()
                } else {
                    //无数据
                    onPageEmpty()
                }
            }
            is Resource.Loading -> {
                onPageLoading()
            }
            is Resource.Error -> {
                onPageError(status.data!!)
            }
        }
    }

    open fun onPageEmpty() {
        Log.e("page", "empty")
        mStatusView.showEmpty()
    }

    open fun onPageError(data: Boolean) {
        refresh.finishRefresh()
        if (data) {
            //初始数据异常
            Log.e("page", "init error")
            mStatusView.showError()
        } else {
            //分页数据异常
        }
        Log.e("page", "loadmore error")
    }

    open fun onPageLoading() {
        refresh.autoRefreshAnimationOnly()
//        mStatusView.showLoading()
    }

    open fun onPageLoaded() {
        Log.e("page", "loaded")
        mStatusView.showContent()
    }

    fun doRefresh() {
        viewModel.refresh()
    }

    override fun subscribeUI(data: T?) {
        // todo
    }

    override fun observableUI(observer: Observer<Resource<T>>) {
        observableUI()
    }

    open fun enabledRefresh(): Boolean = true
    open fun enabledHeader(): Boolean = false

    open fun providePageFactory(): ViewModelProvider.Factory {
        return LibraryInjectorUtil.providerPageViewModelFactory(provideRepository())
    }

    abstract fun bindAdapter(): BasePageAdapter<T>

    abstract fun provideRepository(): BasePageRepository<T>

    abstract fun onItemClick(data: T, position: Int)
}