package com.example.mybaselibrary.utils

import android.graphics.Bitmap
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

/**
 *Create by lvhaoran
 *on 2019/9/4
 */
class BitmapUtil {
    companion object {
        fun saveBitmap(bmp: Bitmap?, bitmapName: String): String {
            var path = ""
            if (bmp == null) {
                return path
            }
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
                // 获得存储卡的路径
                val sdpath = Environment.getExternalStorageDirectory().toString() + "/"

                val f = File(sdpath, bitmapName)

                try {
                    f.createNewFile()
                } catch (e1: IOException) {
                    e1.printStackTrace()
                }

                var fOut: FileOutputStream? = null
                try {
                    fOut = FileOutputStream(f)
                    bmp.compress(Bitmap.CompressFormat.PNG, 50, fOut)
                    path = sdpath + bitmapName
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }

                try {
                    fOut!!.flush()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                try {
                    fOut!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            return path
        }
    }
}