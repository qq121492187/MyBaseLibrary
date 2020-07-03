package com.example.mybaselibrary.net.resource

import androidx.lifecycle.MutableLiveData

/**
 *Create by lvhaoran
 *on 2019/9/20
 */

sealed class Resource<out T>(
    val code: Int = 0,
    val message: String = "",
    val data: T? = null
) {


    class Success<T>(data: T?, code: Int = 1, message: String = "success") :
        Resource<T>(code, message, data)

    class Loading<T>(data: T? = null) : Resource<T>(0, "loading", data)

    class Error<T>(message: String, data: T? = null, code: Int = 2) :
        Resource<T>(code, message, data)


    fun isSuccess(): Boolean = code == 1 || this is Success
    fun subscribe(success: (data: T) -> Unit, error: (msg: String) -> Unit) {
        if (isSuccess())
            success.invoke(this.data!!)
        else
            error.invoke(this.message)
    }

    fun <M> map(change: Resource<T>.() -> M?): Resource<M> {
        val changeData = change.invoke(this)
        return when (this) {
            is Success -> Success(changeData!!)
            is Loading -> Loading()
            else -> Error(this.message)
        }
    }
}