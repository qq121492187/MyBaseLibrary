package com.example.mybaselibrary.views.dialog

import android.app.Dialog
import android.content.Context
import androidx.core.content.ContextCompat
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.example.mybaselibrary.R
import com.example.mybaselibrary.extentions._onClickWithoutFast
import com.example.mybaselibrary.extentions.dp2px
import org.jetbrains.anko.find

/**
 *Create by lvhaoran
 *on 2019/7/30
 *
 * dialog dsl
 */
class BaseDialog(val mContext: Context) {

    private var mDialog = initDialog()
    private lateinit var mContentView: View

    private fun initDialog(): Dialog {
        return Dialog(mContext).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            val lp = window!!.attributes
            lp.gravity = Gravity.CENTER //位置
            lp.width = WindowManager.LayoutParams.WRAP_CONTENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            window?.attributes = lp
        }
    }

    fun create(block: BaseDialog.() -> Unit): Dialog {
        block.invoke(this)
        return mDialog
    }

    /**
     * show语句
     * 语句块内可使用其他dsl语句
     */
    fun show(block: BaseDialog.() -> Unit) {
//        setCustomView()
        block.invoke(this)
        mDialog.show()
    }

    /**
     * init语句
     * 初始化dialog
     * init语句需要放在setcustomview 后面才能生效
     */
    fun init(block: (dialog: Dialog) -> Unit) {
        block.invoke(mDialog)
    }

    fun defaultConfig(dialog: Dialog) {
        val attributes = dialog.window!!.attributes
        attributes.width = -1
        dialog.window!!.attributes = attributes
        dialog.window!!.decorView.setPadding(16.dp2px(), 16.dp2px(), 16.dp2px(), 16.dp2px())
    }

    fun bottomConfig(dialog: Dialog) {
        val window = dialog.window
        window?.setGravity(Gravity.BOTTOM)
        val attributes = window!!.attributes
        attributes.width = WindowManager.LayoutParams.MATCH_PARENT
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.attributes = attributes
        window.setWindowAnimations(R.style.dialog_up_down_style)
        window.decorView.setPadding(0, 0, 0, 0)
    }

    /**
     * 自定义view语句
     * 如果使用自定义view 不再建议使用当前basedialog下的dsl扩展语句
     * 推荐继承dialog 重写dsl相关语句
     */
    fun setCustomView(resId: Int , view: View? = null, block: (CustomDSL.(view: View) -> Unit)? = null) {
        mContentView = view ?: LayoutInflater.from(mContext).inflate(resId, null, false)
        mDialog.setContentView(mContentView)
        block?.invoke(CustomDSL(mContentView, mDialog), mContentView)
    }

    /**
     * 可写的dsl的具体语句的实现
     */
    open class TextDSL(private val mTitle: TextView) {
        fun text(text: String? = null, resId: Int = 0) {
            text?.let { mTitle.text = it } ?: run { mTitle.setText(resId) }
        }

        fun textColor(color: Int) {
            mTitle.setTextColor(color)
        }

        fun textSize(size: Float) {
            mTitle.textSize = size
        }
    }

    /**
     * 可点击的dsl具体实现
     */
    class ClickDSL(private val mView: View) : TextDSL(mView as TextView) {
        fun click(dialog: Dialog, block: (dialog: Dialog) -> Unit) {
            mView._onClickWithoutFast { block.invoke(dialog) }
        }
    }

    class CustomDSL(val mParent: View, private val mDialog: Dialog) {
        fun text(resId: Int, block: (view: TextView) -> Unit) {
            block.invoke(mParent.find(resId))
        }

        fun text(resId:Int,text: String){
            mParent.find<TextView>(resId).text = text
        }

        fun img(resId: Int, block: (view: ImageView) -> Unit) {
            block.invoke(mParent.find(resId))
        }

        fun click(resId: Int, block: (view: View, dialog: Dialog) -> Unit) {
            mParent.find<View>(resId)._onClickWithoutFast {
                block.invoke(it, mDialog)
            }
        }
    }

}