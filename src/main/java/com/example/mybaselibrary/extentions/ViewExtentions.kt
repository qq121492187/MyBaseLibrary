package com.example.mybaselibrary.extentions

import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.mybaselibrary.manager.AnimationManager


const val MIN_DELAY_TIME: Int = 500 // 最小间隔500ms
private var lastClickTime: Long = 0

fun isFastClick(): Boolean {
    var flag = false
    val currentTime = System.currentTimeMillis()
    println(lastClickTime)
    if ((currentTime - lastClickTime) >= MIN_DELAY_TIME) {
        flag = true
    }
    lastClickTime = currentTime
    return flag
}

fun isFastClick(task: () -> Unit) {
    val currentTime = System.currentTimeMillis()
    println(lastClickTime)
    if ((currentTime - lastClickTime) >= MIN_DELAY_TIME) {
        task?.invoke()
    }
    lastClickTime = currentTime
}

/**
 * 扩展view点击事件 处理点击过快
 */
fun View._onClickWithoutFast(block: (it: View) -> Unit) {
    this.setOnClickListener { isFastClick { block?.invoke(this@_onClickWithoutFast) } }
}

/**
 * 扩展view Float动画
 */
fun View.ofFloat(builder: AnimationManager.AnimBuilder, vararg float: Float, block: (view: View, value: Float) -> Unit) {
    val ofFloat = AnimationManager.ofFloat(builder, *float) {
        block.invoke(this, it)
    }
}

/**
 * 扩展view Int动画
 */
fun View.ofInt(builder: AnimationManager.AnimBuilder, vararg int: Int, block: (view: View, value: Int) -> Unit) {
    AnimationManager.ofInt(builder, *int) {
        block.invoke(this, it)
    }
}

/**
 * 扩展view argb动画
 */
fun View.ofArgb(builder: AnimationManager.AnimBuilder, vararg values: Int, block: (view: View, value: ValueAnimator) -> Unit) {
    AnimationManager.ofArgb(builder, *values) {
        block.invoke(this, it)
    }
}

/**
 * view绘制完成
 */
fun View.onInflate(block: (view: View) -> Unit) {
    ViewCompat.isLaidOut(this).yes {
        block?.invoke(this)
    }.no {
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                block.invoke(this@onInflate)
            }
        })
    }
}


/**
 * 扩展imageView，glide加载图片
 *
 * @param url   图片地址
 * @param requestOptions    参数设置
 * @param transitionOptions 动画设置，默认淡入淡出动画
 */
fun ImageView.loadWidthGlide(url: String, requestOptions: RequestOptions = RequestOptions(), transitionOptions: TransitionOptions<*, Drawable> = DrawableTransitionOptions()) {
    Glide.with(this.context)
            .load(url)
            .apply(requestOptions)
            .transition(transitionOptions)
            .into(this)
}

/**
 * 扩展RecyclerView.ViewHolder
 *
 * 添加淡入淡出动画
 * @param [,durations] 动画时长
 */
fun androidx.recyclerview.widget.RecyclerView.ViewHolder.withCrossFade(vararg durations: Long) {
    var animTime = 500L
    val toList = durations?.toList()

    if (toList != null && toList.isNotEmpty()) {
        animTime = toList[0]
    }

    this.itemView.ofFloat(AnimationManager.AnimBuilder().apply {
        this.duration = animTime
    }, 0f, 1f) { view, value ->
        view.alpha = value
    }
}

/**
 * 获取imageview的bitmap
 */
fun ImageView.getBitmap(): Bitmap? {
    isDrawingCacheEnabled = true
    val bitmap = Bitmap.createBitmap(drawingCache)
    isDrawingCacheEnabled = false
    return bitmap
}

/**
 * 获取textview行数
 */
fun TextView.getLines(block: (count: Int) -> Unit) {
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            block.invoke(lineCount)
            return false
        }
    })
}
