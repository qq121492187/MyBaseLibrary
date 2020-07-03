package com.example.mybaselibrary.views.constraintlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintHelper
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.mybaselibrary.extentions.ofFloat
import com.example.mybaselibrary.manager.AnimationManager

/**
 * TODO TIP
 *  view显示时候的动画
 */
class ViewEnterAnimationHelper constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintHelper(context, attrs, defStyleAttr) {

    /**
     * View onLayout之前回调
     */
    override fun updatePreLayout(container: ConstraintLayout?) {
        super.updatePreLayout(container)
    }

    /**
     * View onLayout之后回调
     */
    override fun updatePostLayout(container: ConstraintLayout?) {
        super.updatePostLayout(container)
        //获取所有view
        val views = getViews(container)
        //对view进行一些动画处理
        views.forEach {
            startAnimation(it)
        }
    }

    private fun startAnimation(view: View) {
        view.ofFloat(
            AnimationManager.AnimBuilder()
                .buildDuration(500), 0f, 1f
        ) { v, value ->
            v.alpha = value
        }
    }


}