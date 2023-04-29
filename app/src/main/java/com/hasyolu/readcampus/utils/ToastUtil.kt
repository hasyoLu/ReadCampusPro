package com.hasyolu.readcampus.utils

import android.widget.Toast
import com.hasyolu.readcampus.base.ContextProvider

/**
 * 显示通知栏
 */
fun showToast(text: String) {
    Toast.makeText(ContextProvider.mContext, text, Toast.LENGTH_SHORT).show()
}