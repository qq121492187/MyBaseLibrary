package com.example.mybaselibrary.utils

import com.candidate.doupin.refactory.model.repository.BasePageRepository
import com.example.mybaselibrary.mvvm.viewmodel.BasePageViewModelFactory

/**
 * Todo tip
 *  依赖注入工具
 *  也可使用dagger
 */
object LibraryInjectorUtil {

    fun <T> providerPageViewModelFactory(repo: BasePageRepository<T>): BasePageViewModelFactory<T> {
        return BasePageViewModelFactory(repo)
    }

}