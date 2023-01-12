package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.BuildConfig


/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MusicTask(val context: Application) : BaseTask() {

    override fun run() {
        MusicManager.instance.init(context, BuildConfig.DEBUG)
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return false
    }


}