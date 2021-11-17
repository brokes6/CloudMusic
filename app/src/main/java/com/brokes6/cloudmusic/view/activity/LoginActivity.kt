package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import com.brokes6.cloudmusic.databinding.ActivityLoginBinding
import com.brokes6.cloudmusic.viewmodel.LoginViewModel
import com.laboratory.baseclasslib.base.BaseActivity

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class LoginActivity : BaseActivity<LoginViewModel, ActivityLoginBinding>() {

    override val binding: ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun initView(savedInstanceState: Bundle?) {
        handleData()

    }

    override fun initData() {

    }

    private fun handleData() {
        mViewModel.mLoginInfo.observe(this, {

        })
    }

}