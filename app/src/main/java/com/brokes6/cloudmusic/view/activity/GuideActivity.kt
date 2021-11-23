package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import android.view.View
import com.brokes6.cloudmusic.R
import com.brokes6.cloudmusic.databinding.ActivityGuideBinding
import com.brokes6.cloudmusic.viewmodel.LoginViewModel
import com.laboratory.baseclasslib.base.BaseActivity
import com.laboratory.baseclasslib.extensions.startAndFinishActivity
import com.zackratos.ultimatebarx.ultimatebarx.statusBarOnly

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/19
 * Mender:
 * Modify:
 * Description: 引导页面
 */
class GuideActivity : BaseActivity<LoginViewModel, ActivityGuideBinding>(), View.OnClickListener {

    override val binding: ActivityGuideBinding by lazy { ActivityGuideBinding.inflate(layoutInflater) }

    override fun initView(savedInstanceState: Bundle?) {
        statusBarOnly {
            transparent()
        }
        binding.tvPhoneLogin.setOnClickListener(this)
        binding.tvExperience.setOnClickListener(this)
    }

    override fun initData() {

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.tv_phone_login -> {
                startAndFinishActivity<LoginActivity>()
            }
            R.id.tv_experience -> {
                startAndFinishActivity<MainActivity>()
            }
        }
    }

}