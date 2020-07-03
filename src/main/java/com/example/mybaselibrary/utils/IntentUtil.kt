package com.example.mybaselibrary.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

class IntentUtil {
    companion object{
        fun call(context:Context,number:String){
            context.startActivity(Intent(
                    Intent.ACTION_DIAL,
                    Uri.parse("tel:$number")
            ))
        }
    }
}