package com.brokes6.cloudmusic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.cloudmusic.home.manager.MusicManager
import com.cloudmusic.lib_repository.manager.DataBaseManager
import com.tencent.mmkv.MMKV

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var mContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        initComponent()
    }

    private fun initComponent() {
        MusicManager.instance.init(this)
        MMKV.initialize(this)
        DataBaseManager.instance.init(this)
    }
}