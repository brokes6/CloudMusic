package com.cloudmusic.home.activity

import android.os.Bundle
import android.widget.Toast
import com.cloudmusic.home.R
import com.cloudmusic.home.databinding.ActivityMainBinding
import com.cloudmusic.home.viewmodel.MainViewModel
import com.cloudmusic.lib_base.base.BaseActivity

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun initData() {

    }

    override fun onBackPressed() {
        if (!mViewModel.checkExit()) {
            Toast.makeText(this, R.string.main_exit_hint, Toast.LENGTH_SHORT).show()
        }else{
            finish()
        }
    }
}