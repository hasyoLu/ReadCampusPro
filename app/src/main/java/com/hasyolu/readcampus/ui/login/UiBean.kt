package com.hasyolu.readcampus.ui.login

import com.hasyolu.readcampus.R


/**
 * author: haa-zzz
 * time: 2021-7-27
 * [LoginActivity]中 LiveData 用到的数据类
 */

data class CountChange(var textCountNumber : String? = null,
                       var textColor : Int = (R.color.authCodeAfter),
                       var isEnable : Boolean = false )


data class LoginAutoCode(
    var error : String? = null,
    var success : Int? = null,
)


data class LoginResult(
    val success: Boolean = false,
    val error: String? = null
)

/*
 * LiveData中用来判断登录状态的bean
 */
data class LoginFormState(
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val isUserNameValid: Boolean = false,
    val isPasswordValid : Boolean = false
)

data class DataResult(
    val success: Boolean = false,
    val error: String? = null,
    //val personalInformation : PersonalInformation? = null
)
data class ResultData(
    val success: Boolean = false,
    val error: String? = null,
)