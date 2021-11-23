package com.brokes6.cloudmusic.view.activity

import android.os.Bundle
import android.widget.Toast
import com.brokes6.cloudmusic.R
import com.brokes6.cloudmusic.databinding.ActivityMainBinding
import com.brokes6.cloudmusic.viewmodel.MainViewModel
import com.laboratory.baseclasslib.base.BaseActivity

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