package com.example.mybaselibrary.extentions

import android.app.Activity
import android.content.Context
import androidx.core.content.ContextCompat
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult


/**
 * 跳转扩展
 */
inline fun <reified T : Activity> Context._go(vararg params: Pair<String, Any?>) {
    startActivity<T>(*params)
}

inline fun <reified T : Activity> Activity._go(requestCode: Int, vararg params: Pair<String, Any?>) {
    startActivityForResult<T>(requestCode, *params)
}

/**
 * 获取屏幕宽
 */
fun Context.getScreenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

/**
 * 获取屏幕高
 */
fun Context.getScreenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

/**
 * 获取状态栏高度
 *
 * @param context context
 * @return 状态栏高度
 */
fun Context.getStatusBarHeight(): Int {
    // 获得状态栏高度
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 获取color
 */
fun Context.getResColor(resId: Int): Int {
    return ContextCompat.getColor(this, resId)
}



