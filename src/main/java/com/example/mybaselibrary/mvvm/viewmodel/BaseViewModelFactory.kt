package com.example.mybaselibrary.mvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybaselibrary.mvvm.model.repository.BaseRepository

/**
 *Create by lvhaoran
 *on 2019/9/20
 *
 * ViewModel的工厂方法
 * 可以将ViewModel需要的参数通过工厂方法提供
 * ViewModelFactory的作用是将ViewModel所需要的参数传递给他
 */
open class BaseViewModelFactory(
    private val repo: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BaseViewModel(repo) as T
    }
}