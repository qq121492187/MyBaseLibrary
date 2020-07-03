package com.example.mybaselibrary.mvvm.ui

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mybaselibrary.R
import com.example.mybaselibrary.mvvm.viewmodel.DataBindingConfig
import com.example.mybaselibrary.views.zhen2toolbar.ToolBarView
import com.example.mybaselibrary.views.zhen2toolbar.ToolBarViewListener
import com.jaeger.library.StatusBarUtil
import com.example.mybaselibrary.net.resource.Resource
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
abstract class LibraryFragment<T> : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var mBinding: ViewDataBinding
    private val mBindingConfig by lazy { getDataBindingConfig() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (mBindingConfig.enableBinding) {
            val binding: ViewDataBinding =
                DataBindingUtil.inflate(inflater, mBindingConfig.layoutId, container, false)
            binding.lifecycleOwner = this
            mBindingConfig.bindingParams.forEach { key, value ->
                binding.setVariable(key, value)
            }
            mBinding = binding
            mBinding.root
        } else {
            inflater.inflate(mBindingConfig.layoutId, container, false)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUI()
        observableUI(getUiObservable())
    }

    fun getUiObservable(): Observer<Resource<T>> {
        return Observer { res ->
            handleResource<T>(res,
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
    }

    @Deprecated("")
    fun getViewBinding(): ViewDataBinding {
        return mBinding
    }

    open fun initializeUI() {
        val navigationView = mBinding.root.findViewById<ToolBarView>(R.id.navigation)
        navigationView?.setOnNavigationListener(object : ToolBarViewListener {
            override fun onLeftClick() {
                requireActivity().onBackPressed()
            }

            override fun onRightClick() {
            }

            override fun onRightSecondClick() {
            }
        })
    }

    abstract fun observableUI(observer: Observer<Resource<T>>)
    abstract fun subscribeUI(data: T?)
    abstract fun getDataBindingConfig(): DataBindingConfig

    open fun subscribeLoading() {}
    open fun subscribeError(message: String) {}

    protected fun addBindingVariable(variableId: Int, any: Any) {
        mBindingConfig.addBindingParam(variableId, any)
        mBinding.setVariable(variableId, any)
    }

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

    fun setStatusBarColor(color: Int) {
        StatusBarUtil.setColor(requireActivity(), color, 0)
    }


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
        val im =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        view?.let {
            val isActive = im.isActive
            if (isActive) {
                im.hideSoftInputFromWindow(it.windowToken, 0)
            }
        }
    }

    open fun onBackPressed() {
        requireActivity().onBackPressed()
    }

}