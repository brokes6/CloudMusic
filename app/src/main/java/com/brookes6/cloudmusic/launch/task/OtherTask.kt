package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.launch.BaseTask
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout

/**
 * Author: fuxinbo

 * Date: 2023/3/10

 * Description:
 */
class OtherTask(val context: Application) : BaseTask() {

    override fun run() {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            MaterialHeader(
                context
            )
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(
                context
            )
        }
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return false
    }
}