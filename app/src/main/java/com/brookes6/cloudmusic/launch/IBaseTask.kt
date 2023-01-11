package com.brookes6.cloudmusic.launch

/**
 * Author: fuxinbo
 * Date: 2022/4/20
 * Description: 任务基类
 */
interface IBaseTask {

    /**
     * 锁定当前任务，进入等待
     * 当依赖的任务执行完毕后再执行该任务
     */
    fun startLock()

    /**
     * 解锁
     * 解锁后即开始执行当前任务
     */
    fun stopLock()

    /**
     * 任务执行方法，在该方法中执行任务
     */
    fun run()

    /**
     * 当前任务依赖的Task集合
     * 只有依赖的Task执行完毕后才会执行该任务
     */
    fun dependentTaskList(): MutableList<Class<out IBaseTask>>?

    /**
     * 是否需要立即执行
     */
    fun isRunImmediately(): Boolean

    /**
     * 是否执行在主线程上
     */
    fun isOnMainThread(): Boolean

    /**
     * 是否涉及到获取用户隐私
     *
     * @return
     */
    fun isInvolvesPrivacy(): Boolean

}