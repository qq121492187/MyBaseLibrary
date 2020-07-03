package com.example.mybaselibrary.extentions

import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.app.Activity
import android.os.Build
import android.annotation.TargetApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity


/**
 *Create by lvhaoran
 *on 2019/8/7
 *
 * Activity 扩展函数
 */

/**
 *添加fragment
 */
fun FragmentActivity.addFragment(containerId: Int, fragment: Fragment) {
    supportFragmentManager.beginTransaction().add(containerId, fragment).commit()
}

/**
 * Android6.0设置亮色状态栏模式
 */
@TargetApi(Build.VERSION_CODES.M)
 fun setLightStatusBarForM(activity: Activity, dark: Boolean) {
    val window = activity.window
    if (window != null) {
        val decor = window.decorView
        var ui = decor.systemUiVisibility
        if (dark) {
            ui = ui or SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            ui = ui and SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        decor.systemUiVisibility = ui
    }
}

