package com.hasyolu.readcampus.ui.login

/**
 * author : Haa-zzz
 * time : 2021/8/1
 * 在登录时用于判断当前是否可以按下 登录 按钮 ，只有它们两个同时成立时才设置 登录按钮 isEnable = True
 */
//验证码发送失败
var NOTFWTHEAUTOCODE = true
//手机号或验证码输入错误
var INPUTRIGHT = false

// 1表示完成
var NOFINISH = 0
// 2表示未完成
var FINISHED = 1

//从Sp中获取手机号放在这里，避免重复多次获取
var  PHONEMES : String? = null