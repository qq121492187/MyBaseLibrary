package com.example.mybaselibrary.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.RunningAppProcessInfo
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.FileProvider
import java.lang.reflect.InvocationTargetException
import java.util.*

object AppManager {

    private var mApplication: Application? = null
    private const val PERMISSION_ACTIVITY_CLASS_NAME =
        "com.blankj.utilcode.util.PermissionUtils\$PermissionActivity"

    private val ACTIVITY_LIFECYCLE by lazy { ActivityLifecycleImpl() }

    fun init(context: Context) {
        init(context.applicationContext as Application)
    }

    fun init(app: Application) {
        if (mApplication == null) {
            mApplication = app
            this.mApplication?.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
        } else {
            if (app::class.java != mApplication!!::class.java) {
                mApplication!!.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
                ACTIVITY_LIFECYCLE.mActivityList.clear()
                this.mApplication = app
                this.mApplication?.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE)
            }
        }
    }

    fun getApp(): Application {
        return mApplication ?: getApplicationByReflect().also { init(it) }
    }

    private fun getApplicationByReflect(): Application {
        try {

            @SuppressLint("PrivateApi") val activityThread =
                Class.forName("android.app.ActivityThread")

            val thread = activityThread.getMethod("currentActivityThread").invoke(null)

            val app = activityThread.getMethod("getApplication").invoke(thread)
                ?: throw NullPointerException("u should init first")

            return (app as Application)
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        throw NullPointerException("u should init first")
    }

    fun getActivityLifecycle(): ActivityLifecycleImpl {
        return ACTIVITY_LIFECYCLE
    }

    fun getActivityList(): LinkedList<Activity> {
        return ACTIVITY_LIFECYCLE.mActivityList
    }

    fun getTopActivity(): Activity? {
        if (isAppForeground()) {
            return ACTIVITY_LIFECYCLE.getTopActivity()
        }
        return null
    }

    fun isAppForeground(): Boolean {
        val am =
            getApp().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                ?: return false
        val info = am.runningAppProcesses
        if (info == null || info.size == 0) {
            return false
        }
        for (aInfo in info) {
            if (aInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return aInfo.processName == getApp().packageName
            }
        }
        return false
    }

    interface OnAppStatusChangedListener {
        fun onForeground()
        fun onBackground()
    }

    interface OnActivityDestroyedListener {
        fun onActivityDestroyed(activity: Activity?)
    }


    class ActivityLifecycleImpl : Application.ActivityLifecycleCallbacks {

        val mActivityList = LinkedList<Activity>()
        val mStatusListenerMap by lazy { hashMapOf<Any, OnAppStatusChangedListener>() }
        val mDestroyedListenerMap by lazy { hashMapOf<Activity, Set<OnActivityDestroyedListener>>() }

        private var mForegroundCount = 0
        private var mConfigCount = 0
        private var mIsBackground = false

        private fun fixSoftInputLeaks(activity: Activity?) {
            if (activity == null) {
                return
            }
            val imm = getApp().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            val leakViews = arrayOf(
                "mLastSrvView",
                "mCurRootView",
                "mServedView",
                "mNextServedView"
            )

            for (leakView in leakViews) {
                try {
                    val leakViewField =
                        InputMethodManager::class.java.getDeclaredField(leakView)
                    if (!leakViewField.isAccessible) {
                        leakViewField.isAccessible = true
                    }
                    val obj = leakViewField[imm] as? View ?: continue
                    if (obj.rootView === activity.window.decorView.rootView) {
                        leakViewField[imm] = null
                    }
                } catch (ignore: Throwable) { /**/
                }
            }
        }

        private fun setTopActivity(activity: Activity) {
            if (PERMISSION_ACTIVITY_CLASS_NAME == activity.javaClass.name) {
                return
            }
            if (mActivityList.contains(activity)) {
                if (mActivityList.last != activity) {
                    mActivityList.remove(activity)
                    mActivityList.addLast(activity)
                }
            } else {
                mActivityList.addLast(activity)
            }
        }

        fun getTopActivity(): Activity? {
            if (!mActivityList.isEmpty()) {
                val topActivity = mActivityList.last
                if (topActivity != null) {
                    return topActivity
                }
            }
            val topActivityByReflect = getTopActivityByReflect()
            topActivityByReflect?.let { setTopActivity(it) }
            return topActivityByReflect
        }

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            setTopActivity(activity)
        }

        override fun onActivityStarted(activity: Activity) {
            if (!mIsBackground) {
                setTopActivity(activity)
            }
            if (mConfigCount < 0) {
                ++mConfigCount
            } else {
                ++mForegroundCount
            }
        }

        override fun onActivityResumed(activity: Activity) {
            setTopActivity(activity)
            if (mIsBackground) {
                mIsBackground = false
                postStatus(true)
            }
        }

        override fun onActivityPaused(activity: Activity) {
        }

        override fun onActivityStopped(activity: Activity) {
            if (activity.isChangingConfigurations) {
                --mConfigCount
            } else {
                --mForegroundCount
                if (mForegroundCount <= 0) {
                    mIsBackground = true
                    postStatus(false)
                }
            }
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            mActivityList.remove(activity)
            consumeOnActivityDestroyedListener(activity)
            fixSoftInputLeaks(activity)
        }

        fun addOnAppStatusChangedListener(
            any: Any,
            listener: OnAppStatusChangedListener
        ) {
            mStatusListenerMap[any] = listener
        }

        fun removeOnAppStatusChangedListener(any: Any) {
            mStatusListenerMap.remove(any)
        }

        fun removeOnActivityDestroyedListener(activity: Activity?) {
            if (activity == null) {
                return
            }
            mDestroyedListenerMap.remove(activity)
        }

        fun addOnActivityDestroyedListener(
            activity: Activity?,
            listener: OnActivityDestroyedListener?
        ) {
            if (activity == null || listener == null) {
                return
            }
            val listeners: MutableSet<OnActivityDestroyedListener>
            if (!mDestroyedListenerMap.containsKey(activity)) {
                listeners = HashSet()
                mDestroyedListenerMap[activity] = listeners
            } else {
                listeners =
                    mDestroyedListenerMap[activity] as MutableSet<OnActivityDestroyedListener>
                if (listeners.contains(listener)) {
                    return
                }
            }
            listeners.add(listener)
        }

        private fun postStatus(isForeground: Boolean) {
            if (mStatusListenerMap.isEmpty()) {
                return
            }
            for (onAppStatusChangedListener in mStatusListenerMap.values) {
                if (isForeground) {
                    onAppStatusChangedListener.onForeground()
                } else {
                    onAppStatusChangedListener.onBackground()
                }
            }
        }

        private fun consumeOnActivityDestroyedListener(activity: Activity) {
            val iterator: MutableIterator<Map.Entry<Activity, Set<OnActivityDestroyedListener>>> =
                mDestroyedListenerMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry: Map.Entry<Activity, Set<OnActivityDestroyedListener>> =
                    iterator.next()
                if (entry.key === activity) {
                    val value: Set<OnActivityDestroyedListener> = entry.value
                    for (listener in value) {
                        listener.onActivityDestroyed(activity)
                    }
                    iterator.remove()
                }
            }
        }

        private fun getTopActivityByReflect(): Activity? {
            try {
                @SuppressLint("PrivateApi") val activityThreadClass =
                    Class.forName("android.app.ActivityThread")
                val currentActivityThreadMethod =
                    activityThreadClass.getMethod("currentActivityThread").invoke(null)
                val mActivityListField =
                    activityThreadClass.getDeclaredField("mActivityList")
                mActivityListField.isAccessible = true
                val activities =
                    mActivityListField[currentActivityThreadMethod] as Map<*, *>

                for (activityRecord in activities.values) {
                    val activityRecordClass: Class<*> = activityRecord!!::class.java
                    val pausedField =
                        activityRecordClass.getDeclaredField("paused")
                    pausedField.isAccessible = true
                    if (!pausedField.getBoolean(activityRecord)) {
                        val activityField =
                            activityRecordClass.getDeclaredField("activity")
                        activityField.isAccessible = true
                        return activityField[activityRecord] as Activity
                    }
                }
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchFieldException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            return null
        }

    }

    class FileProvider4UtilCode : FileProvider() {
        override fun onCreate(): Boolean {
            init(context!!)
            return true
        }
    }
}