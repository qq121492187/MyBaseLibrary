package com.example.mybaselibrary.mvvm.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.example.mybaselibrary.R
import com.example.mybaselibrary.extentions.getResColor
import com.example.mybaselibrary.extentions.setLightStatusBarForM
import com.example.mybaselibrary.manager.StackManager
import com.example.mybaselibrary.mvvm.viewmodel.DataBindingConfig
import com.example.mybaselibrary.views.zhen2toolbar.ToolBarView
import com.example.mybaselibrary.views.zhen2toolbar.ToolBarViewListener
import com.jaeger.library.StatusBarUtil
import com.example.mybaselibrary.net.resource.Resource
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *Create by lvhaoran
 *on 2019/9/20
 *
 * activity基类
 */
abstract class LibraryActivity<T> : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    ToolBarViewListener {

    private lateinit var mBinding: ViewDataBinding
    private val mBindingConfig by lazy { getDataBindingConfig() }
    protected var toolBarView: ToolBarView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StackManager.addActivity(this)
        if (mBindingConfig.enableBinding) {
            initDataBinding()
        } else {
            setContentView(mBindingConfig.layoutId)
        }
        initializeUI()
        if (autoObservableUi()) {
            val uiObservable = Observer<Resource<T>> { res ->
                handleResource(res,
                    action = {
                        subscribeUI(it)
                    },
                    loading = {
                        subscribeLoading()
                    },
                    error = {
                        subscribeError(res.message)
                    })
            }
            observableUI(uiObservable)
        }
    }

    private fun initDataBinding() {
        val binding: ViewDataBinding = DataBindingUtil.setContentView(this, mBindingConfig.layoutId)
        binding.lifecycleOwner = this
        mBindingConfig.bindingParams.forEach { key, value ->
            binding.setVariable(key, value)
        }
        this.mBinding = binding
    }

    @Deprecated("")
    fun getViewBinding(): ViewDataBinding {
        return mBinding
    }

    abstract fun observableUI(observer: Observer<Resource<T>>)
    abstract fun subscribeUI(data: T?)

    open fun autoObservableUi(): Boolean = true

    open fun initializeUI() {
        toolBarView = findViewById(R.id.navigation)
        toolBarView?.setOnNavigationListener(this)
        initStatusBar()
    }

    abstract fun getDataBindingConfig(): DataBindingConfig

    protected fun addBindingVariable(variableId: Int, any: Any?) {
        mBindingConfig.addBindingParam(variableId, any)
        mBinding.setVariable(variableId, any)
    }

    open fun initStatusBar() {
        setStatusBarDartMode()
        setStatusBarColor(getStatusBarColor())
    }

    /**
     * 设置充满屏幕
     * 需要重写initStatusBar,调用initStatusBarFitWindows
     * @param needOffsetView 指定需要下沉的View
     */
    open fun initStatusBarFitWindows(needOffsetView: View?) {
        StatusBarUtil.setTranslucentForImageView(this, 0, needOffsetView)
        setStatusBarDartMode()
    }

    fun setStatusBarDartMode() {
        setLightStatusBarForM(this, true)
    }

    fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColor(this, color, 0)
    }

    open fun getStatusBarColor(): Int {
        return getResColor(R.color.colorPrimary)
    }

    open fun subscribeLoading() {}
    open fun subscribeError(message: String) {}

    open fun <A> handleResource(
        resource: Resource<A>,
        error: (() -> Unit)? = null,
        loading: (() -> Unit)? = null,
        action: (data: A?) -> Unit
    ) {
        when (resource) {
            is Resource.Success -> {
                action.invoke(resource.data)
            }
            is Resource.Loading -> {
                loading?.invoke()
            }
            is Resource.Error -> {
                error?.invoke()
            }
        }
    }

    override fun onLeftClick() = onBackPressed()

    override fun onRightClick() {}

    override fun onRightSecondClick() {}

    fun requestPermissions(requestCode: Int, perms: Array<String>) {
        EasyPermissions.requestPermissions(this, "需要以下权限，请允许", requestCode, *perms)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //权限申请失败
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str).append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        //用户点击拒绝并且不在询问时
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this)
                .setRationale("此功能需要${sb}权限，否则无法正常使用，是否打开设置")
                .setPositiveButton("打开")
                .setNegativeButton("关闭")
                .build()
                .show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //权限申请成功
        Log.i("EasyPermissions", "获取成功的权限\$perms")
    }

    fun hideSoftWindow(view: View?) {
        val im = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            val isActive = im.isActive
            if (isActive) {
                im.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        StackManager.removeActivity(this)
    }
}