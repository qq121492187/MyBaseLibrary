package com.example.mybaselibrary.adapter

import androidx.recyclerview.widget.RecyclerView

/**
 *Create by lvhaoran
 *on 2019/9/23
 *
 * pageAdapter数据观察代理
 */
class PageAdapterDataObserverProxy(
    private val dataObserver: RecyclerView.AdapterDataObserver,
    private val headerCount: Int
) : RecyclerView.AdapterDataObserver() {
    override fun onChanged() {
        dataObserver.onChanged()
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        dataObserver.onItemRangeChanged(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
        dataObserver.onItemRangeChanged(positionStart + headerCount, itemCount, payload)
    }

    // 当第n个数据被获取，更新第n+1个position
    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        dataObserver.onItemRangeInserted(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        dataObserver.onItemRangeRemoved(positionStart + headerCount, itemCount)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition + headerCount, toPosition + headerCount, itemCount)
    }
}