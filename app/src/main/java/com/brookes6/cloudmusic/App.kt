package com.brookes6.cloudmusic

import android.app.Application
import com.brookes6.cloudmusic.launch.AppStartUtil
import com.brookes6.cloudmusic.launch.task.MmkvTask
import com.brookes6.cloudmusic.launch.task.MusicTask
import com.brookes6.cloudmusic.launch.task.NetTask
import com.brookes6.cloudmusic.launch.task.RoomTask

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppStartUtil.Instance
            .addTask(MmkvTask(this))
            .addTask(NetTask(this))
            .addTask(RoomTask(this))
            .addTask(MusicTask(this))
            .startTask()
    }
}