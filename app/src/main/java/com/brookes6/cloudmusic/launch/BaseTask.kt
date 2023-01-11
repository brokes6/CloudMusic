package com.brookes6.cloudmusic.launch

import com.brookes6.cloudmusic.utils.LogUtils
import java.util.concurrent.CountDownLatch

/**
 * Author: 付鑫博
 * Date: 2022/4/16
 * Description: 任务Task基类
 */
abstract class BaseTask : IBaseTask {

    private var TAG = this::class.java.simpleName

    // 依赖Task数量，只有依赖的任务都执行完了，才会执行当前任务
    private val mCurCountDownLatch = CountDownLatch(dependentTaskList()?.size ?: 0)

    /**
     * 等待依赖任务执行
     */
    override fun startLock() {
        try {
            LogUtils.d(
                "${this::class.java.simpleName} 等待依赖任务执行，依赖数为：${mCurCountDownLatch.count}"
            )
            mCurCountDownLatch.await()
        } catch (e: Exception) {
            e.printStackTrace()
            LogUtils.e(TAG, "${this::class.java.simpleName} 等待依赖异常：${e.message}")
        }
    }

    /**
     * 执行完一个依赖任务执行，计数值-1
     */
    override fun stopLock() {
        mCurCountDownLatch.countDown()
        LogUtils.d(TAG, "${this::class.java.simpleName} 依赖任务执行完毕，剩余依赖数：${mCurCountDownLatch.count}")
    }

    /**
     * 当前任务的依赖任务
     *
     * @return
     */
    override fun dependentTaskList(): MutableList<Class<out IBaseTask>>? {
        return null
    }

    override fun isInvolvesPrivacy(): Boolean {
        return false
    }


}