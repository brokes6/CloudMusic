package com.brokes6.cloudmusic

import android.os.Bundle
import com.brokes6.cloudmusic.databinding.ActivityMainBinding
import com.brokes6.cloudmusic.viewmodel.MainViewModel
import com.laboratory.baseclasslib.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }
}