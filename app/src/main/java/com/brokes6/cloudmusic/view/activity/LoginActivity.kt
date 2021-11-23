package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.brokes6.cloudmusic.R
import com.brokes6.cloudmusic.databinding.ActivityLoginBinding
import com.brokes6.cloudmusic.viewmodel.LoginViewModel
import com.laboratory.baseclasslib.base.BaseActivity
import com.laboratory.baseclasslib.extensions.startAndFinishActivity
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>(), View.OnClickListener {

    private var isLogin: Boolean = false

    private var mUserPhone: String = ""

    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun initView(savedInstanceState: Bundle?) {
        statusBarOnly {
            color = ContextCompat.getColor(this@LoginActivity, R.color.white)
            light = true
        }
        initChildView()
        handleData()
    }

    override fun initData() {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_next -> {
                if (TextUtils.isEmpty(binding.etPhones.text.toString())) {
                    Toast.makeText(this, R.string.login_phone_error, Toast.LENGTH_SHORT).show()
                    return
                }
                mUserPhone = binding.etPhones.text.toString()
                switchLoginView(true)
            }
            R.id.tv_login -> {
                if (TextUtils.isEmpty(binding.etPasswords.text.toString())) {
                    Toast.makeText(this, R.string.login_password_error, Toast.LENGTH_SHORT).show()
                    return
                }
                mViewModel.login(mUserPhone, binding.etPasswords.text.toString())
            }
            R.id.iv_back -> {
                back()
            }
        }
    }

    override fun onBackPressed() {
        if (isLogin) {
            switchLoginView(false)
            return
        }
        super.onBackPressed()
    }

    private fun initChildView() {
        binding.tvNext.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
        binding.ivBack.setOnClickListener(this)
    }

    private fun handleData() {
        mViewModel.mLoginInfo.observe(this, {
            if (it != null) {
                mViewModel.saveUserInfo(it)
                startAndFinishActivity<MainActivity>()
                mViewModel.saveLoginState(true)
            }
        })

        mViewModel.mError.observe(this, {
            Toast.makeText(this, "账号或密码错误！", Toast.LENGTH_SHORT).show()
        })

    }

    /**
     * 统一的后退处理
     *
     */
    private fun back() {
        if (isLogin) {
            switchLoginView(false)
            return
        }
        finish()
    }

    /**
     * 根据状态来更改UI
     *
     * @param isLogin
     */
    private fun switchLoginView(isLogin: Boolean) {
        this.isLogin = isLogin
        if (isLogin) {
            binding.tvNext.visibility = View.GONE
            binding.etPhones.visibility = View.GONE
            binding.tvLoginTitle.visibility = View.GONE
            binding.tvLogin.visibility = View.VISIBLE
            binding.tv86.visibility = View.GONE
            binding.etPasswords.visibility = View.VISIBLE
            binding.ivBack.setImageResource(R.drawable.ic_back)
        } else {
            binding.tvNext.visibility = View.VISIBLE
            binding.etPhones.visibility = View.VISIBLE
            binding.tv86.visibility = View.VISIBLE
            binding.tvLoginTitle.visibility = View.VISIBLE
            binding.tvLogin.visibility = View.GONE
            binding.etPasswords.visibility = View.GONE
            binding.ivBack.setImageResource(R.drawable.ic_login_close)
        }
    }

}