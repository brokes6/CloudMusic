package com.brookes6.cloudmusic.launch

import com.brookes6.cloudmusic.utils.LogUtils

/**
 * Description: 启动任务执行Runnable
 */
class TaskRunnable(var task: BaseTask, var launcherManager: AppStartUtil?) : Runnable {

    private var TAG = "AppStartUtil"

    override fun run() {
        // Task依赖执行完毕，开始执行该任务
        val startTime = System.currentTimeMillis()
        LogUtils.d("${task::class.java.simpleName} 等待依赖Task执行", TAG)
        // 等待Task依赖执行
        task.startLock()
        LogUtils.d("${task::class.java.simpleName} 开始执行", TAG)
        val realStartTime = System.currentTimeMillis()
        task.run()
        LogUtils.d(
            "${task::class.java.simpleName} 执行完成，是否在主线程：${task.isOnMainThread()}（不包含依赖执行）耗时：${System.currentTimeMillis() - realStartTime}ms",
            TAG
        )

        launcherManager?.run {
            // 执行完毕后通知所有以该Task为依赖的任务，并将这些任务的计数值-1
            notifyTaskFinish(task)
        }
        LogUtils.i(
            "${task::class.java.simpleName} 执行完成，是否在主线程：${task.isOnMainThread()} 总耗时：${System.currentTimeMillis() - startTime}ms",
            TAG
        )
    }
}