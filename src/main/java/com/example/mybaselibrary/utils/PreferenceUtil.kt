package com.example.mybaselibrary.utils

import com.example.mybaselibrary.manager.PreferenceManager


/**
 * SharedPreference工具类
 *
 * PreferentManager由于委托属性，需要重复创建
 * 委托属性较多时或只修改属性值时使用PreferenceUtil，
 *
 * 轻量型Preference属性委托管理请使用 { manager.PreferenceManager)}
 */
object PreferenceUtil {

    /**
     * PreferenceUtil 内部使用PreferenceManager实现
     * 但只创建一个实例，并且不使用委托
     */
    private val mPreferenceManager by lazy { PreferenceManager<Any>("", "") }

    /**
     * 将key value 映射到Preference表里
     * 支持多键值对存储
     */
    fun put(vararg pair: Pair<String, Any>) {
        pair.forEach {
            mPreferenceManager.putOnPreferences(it.first, it.second)
        }
    }

    /**
     * 存储单一键值对
     */
    fun putOne(key: String, value: Any) {
        put(key to value)
    }

    /**
     * 根据key获取value
     * 获取到的value需要强制（即 as）为所需类型
     */
    fun <T> get(key: String, default: Any): T {
        return mPreferenceManager.getOnPreferences(key, default) as T
    }
}