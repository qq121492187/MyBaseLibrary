package com.example.mybaselibrary.adapter

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

/**
 * image加载url图片
 */
@BindingAdapter(value = ["imageUrl", "placeHolder"], requireAll = false)
fun loadImageForUrl(imageView: ImageView, url: String?, placeHolder: Drawable?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(placeHolder)
            .into(imageView)
    }
}

/**
 * 绑定adapter
 */
@BindingAdapter("app:bindAdapter")
fun bindAdapter(rv: RecyclerView, adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
    rv.adapter = adapter
}

/**
 * 控制显示与隐藏
 */
@BindingAdapter("visible")
fun visible(view: View, visible: Boolean) {
    view.visibility = if (visible) View.VISIBLE else View.GONE
}