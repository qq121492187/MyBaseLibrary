package com.example.mybaselibrary.mvvm.viewmodel

import android.util.SparseArray

class DataBindingConfig(val layoutId: Int) {

    val bindingParams by lazy { SparseArray<Any>() }
    var enableBinding = true

    fun addBindingParam(variableId: Int, any: Any?): DataBindingConfig {
        bindingParams.append(variableId, any)
        return this
    }
}