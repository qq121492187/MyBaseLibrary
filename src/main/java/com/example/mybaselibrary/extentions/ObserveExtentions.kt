package com.example.mybaselibrary.extentions

import com.example.mybaselibrary.net.resource.Resource


/**
 * Resource行为事件处理
 *
 * @param res 处理的resource事件
 * @param action 处理成功触发的事件
 * @param error 处理失败的触发事件，默认弹出错误提示
 * @param loading 处理加载中的触发事件，默认弹出加载提示
 */
fun <A> handleResourceExt(
    res: Resource<A>,
    error: ((code: Int, msg: String) -> Unit)? = null,
    loading: ((code: Int) -> Unit)? = null,
    action: (data: A?) -> Unit
) {
    when (res) {
        is Resource.Success -> {
            action.invoke(res.data)
        }
        is Resource.Loading -> {
            loading?.invoke(res.code)
        }
        is Resource.Error -> {
            error?.invoke(res.code, res.message)
        }
    }

}