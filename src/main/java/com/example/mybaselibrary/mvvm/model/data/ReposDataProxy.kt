package com.example.mybaselibrary.mvvm.model.data

abstract class ReposDataProxy<T> {

    abstract fun getCode(): Int
    abstract fun defineSuccessCode(): Int
    abstract fun getMsg(): String
    abstract fun getData(): T?
}