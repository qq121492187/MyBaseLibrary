package com.example.mybaselibrary.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

/**
 *Create by lvhaoran
 *on 2019/8/2
 */
class CoroutineManager {
    companion object {
        fun createMainScope(): CoroutineScope {
            return MainScope()
        }
    }
}