package com.hasyolu.readcampus.ui.login

import com.hasyolu.readcampus.utils.SpUtil

object LoginHelper {
    fun saveLoginInformationToSp( phone : String , isLogin : Boolean){
        SpUtil.setStringValue("number", phone)
        SpUtil.setBooleanValue("isLogin",isLogin)
    }
    fun isLoggedIn() : Boolean{
        return SpUtil.getBooleanValue("isLogin",false)
    }

    fun reSetLogin() {
        saveLoginInformationToSp("", false)
    }

    fun querySpNumber() : String?{
        return SpUtil.getStringValue("number", null)
    }
}
