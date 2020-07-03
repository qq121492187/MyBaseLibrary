package com.example.mybaselibrary.extentions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mybaselibrary.net.resource.Resource

///别名

typealias LiveResource<T> = LiveData<Resource<T>>

typealias MutableLiveResource<T> = MutableLiveData<Resource<T>>