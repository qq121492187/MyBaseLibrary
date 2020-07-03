package com.example.mybaselibrary.mvvm.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.mybaselibrary.R
import com.example.mybaselibrary.databinding.ActivityContainerBinding
import com.example.mybaselibrary.extentions._go
import com.example.mybaselibrary.mvvm.viewmodel.DataBindingConfig
import com.example.mybaselibrary.net.resource.Resource

class ContainerActivity : LibraryActivity<Any>() {

    private val title by lazy { intent.getStringExtra("title") }
    private val mBinding by lazy { getViewBinding() as ActivityContainerBinding }

    companion object {
        var fragment: Fragment? = null
        fun goAndContainerFragment(
            context: Context,
            fragment: Fragment,
            args: Bundle? = null,
            title: String? = null
        ) {
            this.fragment = fragment
            context._go<ContainerActivity>("args" to args, "title" to title)
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_container)
    }

    override fun initStatusBar() {
        if (title == null) {
            initStatusBarFitWindows(null)
        } else {
            super.initStatusBar()
        }
    }

    override fun initializeUI() {
        super.initializeUI()
        if (title != null) {
            toolBarView?.visibility = View.VISIBLE
            toolBarView?.setTextTitle(title)
        } else {
            toolBarView?.visibility = View.GONE
        }
        fragment?.let {
            it.arguments = intent.getBundleExtra("args") ?: null
            supportFragmentManager.beginTransaction().add(R.id.container, it).commit()
        }
    }

    override fun observableUI(observer: Observer<Resource<Any>>) {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
//        if (fragment != null && fragment is LibraryFragment<*>) {
//            (fragment as LibraryFragment<*>).onBackPressed()
//        } else {
//            super.onBackPressed()
//        }
        super.onBackPressed()
    }

    override fun subscribeUI(data: Any?) {
        
    }
}