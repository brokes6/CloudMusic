package com.brookes6.cloudmusic

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.brookes6.cloudmusic.launch.AppStartUtil
import com.brookes6.cloudmusic.launch.task.*
import com.brookes6.cloudmusic.vm.TokenViewModel

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var content: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var app: App

        lateinit var token: TokenViewModel
    }

    private val netTask: NetTask by lazy(LazyThreadSafetyMode.NONE) { NetTask(this) }

    override fun onCreate() {
        super.onCreate()
        content = this
        app = this
        AppStartUtil.Instance
            .addTask(MmkvTask(this))
            .addTask(netTask)
            .addTask(RoomTask(this))
            .addTask(MusicTask(this))
            .startTask()
        AppStartUtil.Instance.startLockMainThread()
    }

    /**
     * 为自动刷新Token设置TokenVM，为了统一监听Token刷新
     */
    fun setTokenVM(tokenVM: TokenViewModel) {
        token = tokenVM
        netTask.setTokenVM(tokenVM)
    }
}