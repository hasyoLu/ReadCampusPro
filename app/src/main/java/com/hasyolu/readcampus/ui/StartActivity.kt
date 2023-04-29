package com.hasyolu.readcampus.ui

import com.hasyolu.readcampus.R
import com.hasyolu.readcampus.base.BaseActivity
import com.hasyolu.readcampus.ui.login.LoginActivity
import com.hasyolu.readcampus.ui.login.LoginHelper
import com.hasyolu.readcampus.ui.main.MainActivity
import kotlinx.coroutines.*

class StartActivity: BaseActivity() {
    /**
     * 获取界面id
     */
    override fun getRLayout():Int{
        return R.layout.activity_start
    }

    /**
     * 初始化界面
     */
    override fun initView(){}

    /**
     * 初始化监听
     */
    override fun initListener(){}

    /**
     * 初始化数据
     */
    override fun initData(){
        // 主线程
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000)
            if (LoginHelper.isLoggedIn()) {
                MainActivity.startFromActivity(this@StartActivity)
            } else {
                LoginActivity.startFromActivity(this@StartActivity)
            }
            delay(500)
            //完成
            finish()
        }
    }
}