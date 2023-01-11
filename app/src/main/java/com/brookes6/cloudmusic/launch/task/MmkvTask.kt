package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.launch.BaseTask
import com.tencent.mmkv.MMKV

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class MmkvTask(val context : Application) : BaseTask(){
    override fun run() {
        MMKV.initialize(context)
    }

    override fun isRunImmediately(): Boolean {
        return true
    }

    override fun isOnMainThread(): Boolean {
        return false
    }
}