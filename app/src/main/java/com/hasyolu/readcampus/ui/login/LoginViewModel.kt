package com.hasyolu.readcampus.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hasyolu.readcampus.R
import com.hasyolu.readcampus.base.BaseViewModel


class LoginViewModel : BaseViewModel() {

    //使ViewModel只对观察者暴露不可修改的LiveData对象
    //监测输入状态改变：
    private val loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = loginForm
    //监测验证码的发送与获取
    private val _loginGetAutoCode = MutableLiveData<LoginAutoCode>()
    val loginGetAutoCode : LiveData<LoginAutoCode> = _loginGetAutoCode
    //监测 登录按钮 与 登录结果
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult
    //监测倒计时
    private val _loginCountNumber = MutableLiveData<CountChange?>()
    val loginCountNumber : LiveData<CountChange?> = _loginCountNumber


    //向外部 暴露 的loginDataChanged方法,当登录状态发生改变后 判断用户名或密码是否合法
    fun loginDataChanged(phone: String, password : String) {
        if (!isValidPhoneNumber(phone)) {
            loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        }else if(! isValidPassword(password) ){
            loginForm.value = LoginFormState(passwordError = R.string.invalid_password,isUserNameValid = true)
        }
        else {
            loginForm.value = LoginFormState(isUserNameValid = true,isPasswordValid = true)
        }
    }
    //发送短信验证码，并在发送成功后 开启倒计时
    fun loginSendCode(username: String){
        bMobSMS(username,_loginGetAutoCode,_loginCountNumber)
    }
    //验证发送的验证码是否正确
    fun loginVerificationResult(phone : String , code : String){
        bMobSMSVerify(phone,code,_loginResult)
    }

}