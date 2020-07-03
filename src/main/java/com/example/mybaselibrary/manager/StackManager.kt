package com.example.mybaselibrary.manager

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import java.util.*

/**
 *Create by lvhaoran
 *on 2019/8/2
 */
object StackManager {
    private val mStack by lazy { Stack<Activity>() }

    fun addActivity(activity: Activity) {
        mStack.add(activity)
    }

    fun removeActivity(activity: Activity) {
        if (mStack.contains(activity)) {
            mStack.remove(activity)
        }
    }

    fun finishActivity(activity: Activity) {
        activity.finish()
        if (mStack.contains(activity)) {
            mStack.remove(activity)
        }
    }

    fun getTopActivity(): Activity {
        return mStack.lastElement()
    }

    fun finishOthersTo(clzName: String) {
        while (true) {
            val topActivity = getTopActivity()
            if (topActivity.javaClass.simpleName == clzName) {
                return
            }
            finishActivity(topActivity)
        }
    }

    fun finishOthersTo(clazz: Class<out Activity>) {
        finishOthersTo(clazz.simpleName)
    }

    fun getActivity(clzName: String): Activity? {
        mStack.forEach {
            if (it.javaClass.simpleName == clzName) {
                return it
            }
        }
        return null
    }

    fun getActivity(clazz: Class<out Activity>):Activity?{
        return getActivity(clazz.simpleName)
    }

    fun clear() {
        mStack.forEach {
            it.finish()
        }
        mStack.clear()
    }
}