package com.hasyolu.readcampus

import android.app.Application
import cn.bmob.v3.Bmob
import com.hasyolu.readcampus.constant.Constant
import dagger.hilt.android.HiltAndroidApp
import java.io.File

/**
 * app对象
 */

@HiltAndroidApp
class App() : Application(){
    /**
     * 创建事件
     */
    override fun onCreate() {
        super.onCreate()
        initBMOB()
        // 初始化文件夹
        if (!File(Constant.BOOK_CACHE_PATH).exists()) {
            File(Constant.BOOK_CACHE_PATH).mkdir()
        }
    }

    private fun initBMOB() {
        Bmob.initialize(this, "c9cdfcef07b2af32b45776233231a8aa")
    }
}
