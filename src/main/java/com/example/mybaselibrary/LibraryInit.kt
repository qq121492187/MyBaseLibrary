package com.example.mybaselibrary

import android.app.Application
import android.content.Context
import com.example.mybaselibrary.manager.AppManager
import com.scwang.smartrefresh.header.MaterialHeader
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter

object LibraryInit {

    private lateinit var libraryConfig: LibraryConfig
    private lateinit var app: Application
    lateinit var appContext: Context

    fun init(app: Application, config: LibraryConfig) {
        this.app = app
        this.appContext = app.baseContext
        this.libraryConfig = config
        AppManager.init(app)
        init()
    }

    fun getLibraryConfig(): LibraryConfig {
        return libraryConfig
    }

    private fun init() {
        if (libraryConfig.useDefaultRefreshUI) {
            SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
                return@setDefaultRefreshHeaderCreator MaterialHeader(context)
            }
            SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
                return@setDefaultRefreshFooterCreator ClassicsFooter(context)
            }
        }
    }


}