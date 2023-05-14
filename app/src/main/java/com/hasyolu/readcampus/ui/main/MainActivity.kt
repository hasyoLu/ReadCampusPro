package com.hasyolu.readcampus.ui.main

import android.Manifest
import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.hasyolu.readcampus.R
import com.hasyolu.readcampus.base.BaseActivity
import com.hasyolu.readcampus.ui.login.LoginActivity
import com.hasyolu.readcampus.ui.login.LoginHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : BaseActivity() {
  @VisibleForTesting
  val viewModel: MainViewModel by viewModels()

  private lateinit var navController: NavController

  /**
   * 获取界面id
   */
  override fun getRLayout():Int{
    return R.layout.activity_main
  }

  /**
   * 初始化界面
   */
  override fun initView() {
    navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main)
    bottom_navigation.setupWithNavController(navController)
    //申请权限
    requestPermission()
  }

  /**
   * 初始化监听
   */
  override fun initListener() {
    navController.addOnDestinationChangedListener { _, destination, _ ->
      when (destination.id) {
        R.id.navigation_squareFragment ->{
          bottom_navigation.visibility = View.GONE
        }
        else -> {
          bottom_navigation.visibility = View.VISIBLE
        }
      }
    }

    //错误通知事件
    viewModel.toast.observe(this, Observer<String> {
      if (!it.isNullOrBlank()) {
        Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
      }
    })
  }
  /**
   * 初始化数据
   */
  override fun initData(){

  }

  /**
   * 申请权限
   */
  private fun requestPermission(){
    val permission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
      if(!it) {
        viewModel.toastMsg("请开通相关权限")
      }
    }
    //申请权限
    permission.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
  }

  fun goToLoginActivity() {
    LoginHelper.reSetLogin()
    LoginActivity.startFromActivity(this)
    finish()
  }

  /**
   * 静态内容
   */
  companion object {
    fun startFromActivity(activity: Activity) {
      //设置配置
      val options = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
      //设置启动窗体
      val intent = Intent(activity, MainActivity::class.java)
      //启动窗体
      activity.startActivity(intent,options)

    }
  }
}
