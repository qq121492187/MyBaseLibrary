package com.example.mybaselibrary.utils

import android.os.Environment

class DeviceUtil {
    companion object {

        fun isExitsSdcard(): Boolean {
            return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        }

    }
}