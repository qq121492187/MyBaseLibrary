package com.example.mybaselibrary.extentions

import android.content.res.Resources
import android.util.TypedValue


//类型扩展方法

/**
 * boolean扩展方法
 * 当true时执行传入的任务
 */
fun Boolean.yes(block: () -> Unit): Boolean {
    if (this) block?.invoke()
    return this
}

/**
 * boolean扩展方法
 * 当false时执行传入的任务
 */
fun Boolean.no(block: () -> Unit): Boolean {
    if (!this) block?.invoke()
    return this
}

/**
 * Int扩展方法，方便px和dp之间转换
 */
fun Int.dp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    ).toInt()
}