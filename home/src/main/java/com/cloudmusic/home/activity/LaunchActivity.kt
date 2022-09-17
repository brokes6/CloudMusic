package com.cloudmusic.home.activity

import android.os.Bundle
import android.os.CountDownTimer
import com.cloudmusic.home.databinding.ActivitySplashBinding
import com.cloudmusic.home.viewmodel.LoginViewModel
import com.cloudmusic.lib_base.base.BaseActivity
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
class LaunchActivity : BaseActivity<LoginViewModel, ActivitySplashBinding>() {

    override val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(
            layoutInflater
        )
    }

    override fun initView(savedInstanceState: Bundle?) {
        statusBarOnly {
            transparent()
        }
    }

    override fun initData() {
        startCountDownTime()
    }

    private fun startCountDownTime() {
        val countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                if (mViewModel.isLogin()) {
                    startAndFinishActivity<MainActivity>()
                } else {
                    startAndFinishActivity<GuideActivity>()
                }
            }
        }
        countDownTimer.start()
    }


}