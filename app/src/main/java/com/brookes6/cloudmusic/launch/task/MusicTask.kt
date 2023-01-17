package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.launch.BaseTask
import com.lzx.starrysky.StarrySkyInstall
import com.lzx.starrysky.notification.INotification


/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MusicTask(val context: Application) : BaseTask() {

    override fun run() {
        StarrySkyInstall.init(context).setDebug(BuildConfig.DEBUG)
            .setNotificationSwitch(true)
            .setNotificationType(INotification.SYSTEM_NOTIFICATION)
            .setAutoManagerFocus(true)
            .apply()
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return true
    }
}