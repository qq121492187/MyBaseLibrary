package com.example.mybaselibrary.manager

import android.content.Context
import android.content.pm.ApplicationInfo
import android.os.Build
import android.util.Log
import com.example.mybaselibrary.BuildConfig
import com.example.mybaselibrary.LibraryInit

/**
 *Create by lvhaoran
 *on 2019/8/13
 */
object AppLog {
    private val isDebug = BuildConfig.DEBUG
    private var logKey = this::class.java.simpleName
    private var releaseEnable = false

    fun with(any: Any): AppLog {
        logKey = any::class.java.simpleName
        return this
    }

    fun releaseEnable(enable: Boolean): AppLog {
        return this
    }

    fun error(value: String) {
        error(logKey, value)
    }

    fun error(key: String, value: String) {
        if (isDebug || releaseEnable)
            Log.e(key, value)

        reset()
    }

    fun debug(value: String) {
        debug(logKey, value)
    }

    fun debug(key: String, value: String) {
        if (isDebug || releaseEnable)
            Log.d(key, value)

        reset()
    }

    fun info(value: String) {
        info(logKey, value)
    }

    fun info(key: String, value: String) {
        if (isDebug || releaseEnable)
            Log.i(key, value)

        reset()
    }

    fun verbose(value: String) {
        verbose(logKey, value)
    }

    fun verbose(key: String, value: String) {
        if (isDebug || releaseEnable)
            Log.v(key, value)

        reset()
    }

    private fun reset() {
        releaseEnable = false
    }

    /**
     * 判断当前应用是否是debug状态
     */
    fun isApkInDebug(): Boolean {
        return BuildConfig.DEBUG
    }
}