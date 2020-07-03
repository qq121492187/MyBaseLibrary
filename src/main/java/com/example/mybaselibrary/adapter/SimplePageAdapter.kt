package com.example.mybaselibrary.adapter

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil

abstract class SimplePageAdapter<D, B : ViewDataBinding>(
    context: Context,
    private val layoutId: Int,
    diff: DiffUtil.ItemCallback<D>
) : BasePageAdapter<D>(context, diff) {


    override fun getItemLayoutId(): Int = layoutId

    override fun onBindItem(holder: BaseViewHolder, data: D, position: Int) {
        onBindItem(holder.viewBinding as B, data, position)
    }

    abstract fun onBindItem(itemBinding: B, data: D, position: Int)


}