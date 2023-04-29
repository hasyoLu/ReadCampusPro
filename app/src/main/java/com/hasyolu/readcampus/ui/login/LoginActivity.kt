package com.hasyolu.readcampus.ui.login
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hasyolu.readcampus.R
import com.hasyolu.readcampus.base.BaseActivity
import com.hasyolu.readcampus.databinding.ActivityLoginBinding
import com.hasyolu.readcampus.ui.main.MainActivity

class LoginActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel
    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var phone : EditText
    private lateinit var code : EditText
    private lateinit var login : Button
    private lateinit var loading : ProgressBar
    private lateinit var authCode : TextView


    override fun getRLayout():Int{
        return R.layout.activity_login
    }

    /**
     * 初始化界面
     */
    override fun initView(){
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        //TODO: issues: 由于BaseActivity的getRLayout的封装限制，无法使用DateBinding
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        phone = findViewById(R.id.username)
        code = findViewById(R.id.password)
        login = findViewById(R.id.login)
        loading = findViewById(R.id.loading)
        authCode = findViewById(R.id.authCode)

        val button : Button =  findViewById(R.id.toMainFragment)
        button.setOnClickListener {
            MainActivity.startFromActivity(this)
            finish()
        }

    }

    /**
     * 初始化数据
     */
    override fun initData(){
        initInputData()
        initAuthCodeData()
        initVerifyData()
    }
    private fun initAuthCodeData() {
        authCode.setOnClickListener{
            NOTFWTHEAUTOCODE = false
            loginViewModel.loginSendCode(phone.text.toString().trim() )
        }
        loginViewModel.loginGetAutoCode.observe(this, Observer {
            val loginAutoCode = it ?: return@Observer
            if( loginAutoCode.error != null ){
                Toast.makeText(this,"发送失败", Toast.LENGTH_LONG).show()
            }else{
                login.isEnabled = true
            }
        })
        loginViewModel.loginCountNumber.observe(this, Observer {
            val countNumber = it ?: return@Observer
            if(countNumber.textCountNumber != null){
                authCode.text = countNumber.textCountNumber
            }
            authCode.setBackgroundResource(countNumber.textColor)
            authCode.isEnabled = countNumber.isEnable
        })
    }

    private fun initInputData() {
        //输入发生改变后，调用ViewModel中的方法区判断输入是否有异常
        phone.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                phone.text.toString().trim(),
                code.text.toString().trim()
            )
        }
        code.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                phone.text.toString().trim(),
                code.text.toString().trim()
            )
        }
        loginViewModel.loginFormState.observe(this, Observer {
            val loginState = it ?:  return@Observer
            //根据登录状态 设置 login按钮是否可以点击
            //login.isEnabled = loginState.isDataValid
            if (loginState.usernameError != null) {
                phone.error = getString(loginState.usernameError)
            }
            else {
                //可以发验证码了
                if(NOTFWTHEAUTOCODE){
                    authCode.isEnabled = loginState.isUserNameValid
                    authCode.setBackgroundResource(R.color.authCode)
                }
                INPUTRIGHT = loginState.isUserNameValid && loginState.isPasswordValid
            }
        })
    }

    private fun initVerifyData() {

        login.setOnClickListener{
            if(!INPUTRIGHT){
                showErrorToast(this,getString(R.string.inputError))
                return@setOnClickListener
            }else{
                //点击登录后设置 等待 可见
                loading.visibility = View.VISIBLE
                loginViewModel.loginVerificationResult(phone.text.toString().trim(),
                    code.text.toString().trim())
            }
        }
        loginViewModel.loginResult.observe(this, Observer {
            val result = it ?: return@Observer
            if(result.success){
                showSuccessToast(this,"登录成功")
                MainActivity.startFromActivity(this)
                finish()
            }else{
                showErrorToast(this,getString(R.string.sendAutoCodeError))
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        fun startFromActivity(activity: Activity) {
            //设置启动窗体
            val intent = Intent(activity, LoginActivity::class.java)
            //启动窗体
            activity.startActivity(intent)

        }
    }
}