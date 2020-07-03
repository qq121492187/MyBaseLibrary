package com.example.mybaselibrary.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception

/**
 *Create by lvhaoran
 *on 2019/9/21
 */
abstract class BasePageAdapter<T>(
        val mContext: Context,
        diffCallBack: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, BaseViewHolder>(diffCallBack) {

    private var onClick: ((data: T, position: Int) -> Unit)? = null
    private val mInflater by lazy { LayoutInflater.from(mContext) }
    private var headerData: Any? = null
    private var onViewChangeListener: OnViewChangeListener? = null
    val HeadType = -2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            HeadType -> createHeaderViewHolder(parent)
            else -> createBodyViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (needHeader() && position == 0) {
            onBindHeader(holder as HeaderViewHolder, headerData)
        } else {
            val realPosition = if (needHeader()) position - 1 else position
            holder.viewBinding.root.setOnClickListener {
                if (!interceptItemClick(holder, getDataItem(position), realPosition)) {
                    onClick?.invoke(getDataItem(position)!!, realPosition)
                }
            }
            onBindItem(holder, getDataItem(position)!!, realPosition)
        }
    }

    private fun createHeaderViewHolder(parent: ViewGroup): HeaderViewHolder {
        if (getHeaderLayoutId() == -1) {
            throw Exception("header layout is -1")
        }
        val viewBinding =
                DataBindingUtil.inflate<ViewDataBinding>(mInflater, getHeaderLayoutId(), parent, false)
        return HeaderViewHolder(viewBinding)
    }

    open fun createBodyViewHolder(parent: ViewGroup): BaseViewHolder {
        val viewBinding =
                DataBindingUtil.inflate<ViewDataBinding>(mInflater, getItemLayoutId(), parent, false)
        try {
            viewBinding.lifecycleOwner = mContext as LifecycleOwner
        } catch (e: ClassCastException) {
            Log.e("BasePageAdapter:", "mContext is not LifecycleOwner")
        }
        return BaseViewHolder(viewBinding)
    }

    abstract fun getItemLayoutId(): Int
    abstract fun onBindItem(holder: BaseViewHolder, data: T, position: Int)
    open fun getHeaderLayoutId(): Int = -1
    open fun onBindHeader(holder: HeaderViewHolder, headerData: Any?) {}
    open fun setHeaderData(headerData: Any) {
        this.headerData = headerData
        notifyDataSetChanged()
    }

    override fun registerAdapterDataObserver(observer: RecyclerView.AdapterDataObserver) {
        if (needHeader()) {
            super.registerAdapterDataObserver(PageAdapterDataObserverProxy(observer, 1))
        } else {
            super.registerAdapterDataObserver(observer)
        }
    }

    override fun getItemCount(): Int {
        val currentSize = currentList?.size ?: 0
        return if (needHeader())
            currentSize + 1
        else
            super.getItemCount()
    }

    fun getDataItem(position: Int): T? {
        return if (needHeader()) {
            getItem(position - 1)
        } else {
            if (itemCount == 0) {
                return null
            }
            getItem(position)
        }
    }

    open fun interceptItemClick(holder: BaseViewHolder, data: T?, position: Int): Boolean {
        return false
    }

    override fun getItemViewType(position: Int): Int {
        if (needHeader() && position == 0) return HeadType
        return super.getItemViewType(position)
    }

    open fun needHeader(): Boolean {
        return false
    }

    fun onItemClick(click: (data: T, position: Int) -> Unit) {
        this.onClick = click
    }

    fun setOnViewChangeListener(listener: OnViewChangeListener) {
        this.onViewChangeListener = listener
    }

    override fun onViewAttachedToWindow(holder: BaseViewHolder) {
        super.onViewAttachedToWindow(holder)
        onViewChangeListener?.onChildViewAttachedToWindow(holder, holder.layoutPosition)
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder) {
        super.onViewDetachedFromWindow(holder)
        onViewChangeListener?.onChildViewDetachedFromWindow(holder, holder.layoutPosition)
    }

    class HeaderViewHolder(viewBinding: ViewDataBinding) : BaseViewHolder(viewBinding)

    interface OnViewChangeListener {
        fun onChildViewAttachedToWindow(holder: BaseViewHolder, position: Int)
        fun onChildViewDetachedFromWindow(holder: BaseViewHolder, position: Int)
    }
}