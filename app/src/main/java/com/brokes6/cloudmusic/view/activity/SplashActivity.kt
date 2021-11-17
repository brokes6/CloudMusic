package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import com.brokes6.cloudmusic.databinding.ActivitySplashBinding
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
class SplashActivity : BaseActivity<LoginViewModel, ActivitySplashBinding>() {

    override val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

}