package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.brokes6.cloudmusic.R
import com.brokes6.cloudmusic.databinding.ActivityLoginBinding
import com.brokes6.cloudmusic.viewmodel.LoginViewModel
import com.laboratory.baseclasslib.base.BaseActivity
import com.laboratory.baseclasslib.extensions.goActivity

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
        handleData()
        initChildView()
    }

    private fun initChildView() {
        binding.tvNext.setOnClickListener(this)
        binding.tvLogin.setOnClickListener(this)
    }

    override fun initData() {

    }

    private fun handleData() {
        mViewModel.mLoginInfo.observe(this, {
            if (it.code == 200) {
                // TODO 保存用户信息
                goActivity<MainActivity>()
            }
        })

        mViewModel.mError.observe(this, {

        })

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
        }
    }

    override fun onBackPressed() {
        if (isLogin) {
            switchLoginView(false)
            return
        }
        super.onBackPressed()
    }

    private fun switchLoginView(isLogin: Boolean) {
        this.isLogin = isLogin
        if (isLogin) {
            binding.tvNext.visibility = View.GONE
            binding.etPhones.visibility = View.GONE
            binding.tvLoginTitle.visibility = View.GONE
            binding.tvLogin.visibility = View.VISIBLE
            binding.etPasswords.visibility = View.VISIBLE
        } else {
            binding.tvNext.visibility = View.VISIBLE
            binding.etPhones.visibility = View.VISIBLE
            binding.tvLoginTitle.visibility = View.VISIBLE
            binding.tvLogin.visibility = View.GONE
            binding.etPasswords.visibility = View.GONE
        }
    }

}