package com.example.mybaselibrary.manager

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.os.Build

/**
 * 动画管理
 */
object AnimationManager {

    fun ofFloat(builder: AnimBuilder, vararg values: Float, block: (value: Float) -> Unit): Animator {
        val animator = ValueAnimator.ofFloat(*values)
        animator.duration = builder.duration
        animator.repeatCount = builder.repeatCount
        builder.interceptor?.let { animator.interpolator = it }
        animator.addUpdateListener {
            val value = it.animatedValue as Float
            block.invoke(value)
        }
        builder.listener?.let {
            animator.addListener(it)
        }
        animator.start()
        return animator
    }

    fun ofInt(builder: AnimBuilder? = null, vararg values: Int, block: (value: Int) -> Unit) {
        val animator = ValueAnimator.ofInt(*values)
        animator.duration = builder?.duration ?: 0
        animator.addUpdateListener {
            val value = it.animatedValue as Int
            block.invoke(value)
        }
        builder?.listener?.let {
            animator.addListener(it)
        }
        animator.start()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun ofArgb(builder: AnimBuilder? = null, vararg values: Int, block: (value: ValueAnimator) -> Unit) {
        val animator = ValueAnimator.ofArgb(*values)
        animator.duration = builder?.duration ?: 0
        animator.addUpdateListener {
            block.invoke(it)
        }
        animator.start()
    }

    class AnimBuilder {
        /**
         * 动画时长
         */
        var duration: Long = 0
        /**
         * 重复次数
         *
         * 无限循环{@link ValueAnimator.INFINITE}
         */
        var repeatCount: Int = 0
        var listener: Animator.AnimatorListener? = null
        var interceptor: TimeInterpolator? = null

        /**兼容JAVA Builder模式**/
        fun buildDuration(duration: Long): AnimBuilder {
            this.duration = duration
            return this@AnimBuilder
        }

        fun buildRepeatCount(repeatCount: Int): AnimBuilder {
            this.repeatCount = repeatCount
            return this@AnimBuilder
        }

        fun buildListener(listener: Animator.AnimatorListener): AnimBuilder {
            this.listener = listener
            return this@AnimBuilder
        }

        fun buildInterceptor(interceptor: TimeInterpolator): AnimBuilder {
            this.interceptor = interceptor
            return this@AnimBuilder
        }

    }

}