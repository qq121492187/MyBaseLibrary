package com.example.mybaselibrary.manager

import android.content.Context
import com.example.mybaselibrary.LibraryInit
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.URLDecoder
import java.net.URLEncoder
import kotlin.reflect.KProperty

/**
 * preferences委托类
 *
 * 委托属性传入PreferenceManager，被委托类对属性值进行存储或读取操作
 * 注意：
 *      由于委托属性的set get 方法委托给了PreferenceManager
 *      故委托属性的值被修改的时候实际上会调用PreferenceManager.setValue方法进行表内的值修改
 *      所以，如果委托属性只是想要获取Preference表内的值，并不会修改表内的值，请将委托属性限定为只读（即 val）
 *      以免误操作
 *
 *  tips:
 *      kotlin委托实现的PreferenceManager由于需要委托一个属性，所以每次委托都会重写创建一个实例
 *      并且PreferenceManager只能通过委托（即 by）形式出现，所以适用性不是太好
 *      但是PreferenceManager的使用较为方便和简洁，所以可以根据场景来使用
 *      推荐在属性少或者属性只读场景下使用
 *      其他场景可使用{ util.PreferenceUtil }来代替
 *
 * @param key 委托属性需要操作的preferences键名
 * @param defaultValue 委托属性的默认值，必须制定默认值，因为需要通过默认值来推断写入或读取的值类型
 */
class PreferenceManager<T>(val key: String, private val defaultValue: T) {

    companion object {
        /**
         * shared id
         */
        private const val PREFERENCE_ID = "shared_preference_id"
        /**
         * sharedpreference实例
         */
        private val mPre by lazy {
            LibraryInit.appContext.getSharedPreferences(PREFERENCE_ID, Context.MODE_PRIVATE)
        }

        /**
         * 清空所有数据
         */
        fun clearAllPreference() {
            mPre.edit().clear().apply()
        }

        /**
         * 清空指定key的内容
         */
        fun clearPreference(key: String) {
            mPre.edit().remove(key).apply()
        }
    }

    /**
     * 委托方法 将获取的值给委托的属性
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getOnPreferences(key, defaultValue)
    }

    /**
     * 委托方法 将委托的属性写入preferences
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putOnPreferences(key, value)
    }

    /**
     * 写入preferences
     */
    fun putOnPreferences(key: String, value: T) = with(mPre.edit()) {
        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> putString(key, serialize(value))
        }.apply()
    }

    /**
     * 读取preferences
     */
    fun getOnPreferences(key: String, default: T): T = with(mPre) {
        val value = when (default) {
            is String -> getString(key, default)
            is Int -> getInt(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            is Boolean -> getBoolean(key, default)
            else -> deSerialize(getString(key, serialize(default))!!)
        }
        return value as T
    }

    /**
     * 查询key是否已存在
     */
    fun container(key: String): Boolean {
        return mPre.contains(key)
    }

    /**
     * 序列化对象
     * 将对象序列化为字符串
     */
    private fun <A> serialize(obj: A): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val objectOutputStream = ObjectOutputStream(byteArrayOutputStream)
        objectOutputStream.writeObject(obj)
        var objStr = byteArrayOutputStream.toString("ISO-8859-1")
        objStr = URLEncoder.encode(objStr, "UTF-8")
        objectOutputStream.close()
        byteArrayOutputStream.close()
        return objStr
    }

    /**
     * 反序列化
     * 将序列化的字符串反序列化为对象
     */
    private fun <A> deSerialize(objStr: String): A {
        val decodeStr = URLDecoder.decode(objStr, "UTF-8")
        val byteArrayInputStream =
            ByteArrayInputStream(decodeStr.toByteArray(charset("ISO-8859-1")))
        val objectInputStream = ObjectInputStream(byteArrayInputStream)
        val obj = objectInputStream.readObject() as A
        objectInputStream.close()
        byteArrayInputStream.close()
        return obj
    }
}