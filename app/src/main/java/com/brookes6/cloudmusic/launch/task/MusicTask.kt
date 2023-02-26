package com.brookes6.cloudmusic.launch.task

import android.app.Application
import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.utils.LogUtils
import com.lzx.starrysky.StarrySkyInstall
import com.lzx.starrysky.notification.INotification


/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MusicTask(private val context: Application) : BaseTask() {

    override fun run() {
        StarrySkyInstall.init(context).setDebug(BuildConfig.DEBUG)
            .setNotificationSwitch(true)
            .setNotificationType(INotification.SYSTEM_NOTIFICATION)
            .setAutoManagerFocus(true)
            .startForegroundByWorkManager(true)
            .connServiceListener(object : ServiceConnection{
                override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                    LogUtils.i("音乐服务启动成功")
                }

                override fun onServiceDisconnected(name: ComponentName?) {
                    LogUtils.i("音乐服务断开连接")
                }
            })
            .apply()
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return true
    }
}