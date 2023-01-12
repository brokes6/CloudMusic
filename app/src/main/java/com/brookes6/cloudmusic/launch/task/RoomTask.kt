package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.manager.DataBaseManager

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class RoomTask(val context : Application) : BaseTask() {
    override fun run() {
        DataBaseManager.instance.init(context)
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return false
    }


}