package com.hasyolu.readcampus.ui.login

import androidx.lifecycle.MutableLiveData
import cn.bmob.v3.BmobSMS
import cn.bmob.v3.exception.BmobException
import cn.bmob.v3.listener.QueryListener
import cn.bmob.v3.listener.UpdateListener

/**
 * author : Haa-zzz
 * time : 2021/8/1
 * 短信验证码请求 以及 验证 功能
 * 在发送短信验证码成功后开启倒计时功能
 */
fun bMobSMS(phoneNumber : String, loginGetAutoCode : MutableLiveData<LoginAutoCode>,
            loginCountNumber : MutableLiveData<CountChange?>) {

    BmobSMS.requestSMSCode(phoneNumber, "", object : QueryListener<Int>() {
        override fun done(smsId : Int?, e: BmobException?) {
            if (smsId != null) {
                loginGetAutoCode.value = LoginAutoCode(success = smsId)
                oneMinuteCountdown(loginCountNumber)
            } else {
                loginGetAutoCode.value = LoginAutoCode(error = e.toString())
            }
        }
    })
}

fun bMobSMSVerify(phone : String , code : String, loginResult : MutableLiveData<LoginResult>){
    BmobSMS.verifySmsCode(phone,code, object : UpdateListener(){
        override fun done(e: BmobException?) {
            if(e == null){
                LoginHelper.saveLoginInformationToSp(phone, true)
                loginResult.value = LoginResult(success = true)
            }else{
                loginResult.value = LoginResult(success = false,error = e.toString())
            }
        }
    })
}

