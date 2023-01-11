package com.brookes6.cloudmusic.launch

import android.os.Looper
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.TaskSortUtils
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger

/**
 * Author: 付鑫博
 * Date: 2022/4/16
 * Description:初始化各种框架工具类
 */
class AppStartUtil private constructor() {

    // 被依赖类的class作为key，以所有依赖它的Task作为value
    private val mDependTaskHashMap = HashMap<Class<out IBaseTask>, MutableList<IBaseTask>>()

    // 主线程依赖子线程Task锁，子线程执行完毕后再执行主线程
    private var mMainThreadCountDownLatch: CountDownLatch? = null

    // 用于处理任务的线程池(内部则是根据CPU核心数来定制的线程个数)
    private var executor: ThreadPoolExecutor? = null

    // 主线程需要等待子线程任务的个数
    private val mMainNeedWaitTaskCount = AtomicInteger()

    // 涉及隐私需要延迟Task锁
    private var mInvolvesPrivacyCountDownLatch: CountDownLatch? = null

    // 涉及隐私需要延迟执行的任务个数
    private var mInvolvesPrivacyTaskCount = AtomicInteger()

    companion object {

        const val TAG = "AppStartUtil"

        // 主线程超时时间
        const val MAIN_THREAD_WAIT_MILLIONS_TIME = 996 * 31L

        @JvmStatic
        val Instance: AppStartUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AppStartUtil()
        }

        // 所有的Task任务
        private var mAllTaskList = mutableListOf<BaseTask>()

        // 涉及隐私政策的子线程执行任务
        private var mInvolvesPrivacyTaskList = mutableListOf<BaseTask>()

        // 涉及隐私政策的主线程执行任务
        private val mInvolvesRunMainTaskList = mutableListOf<BaseTask>()

        // 所有的Task Class列表
        private val mAllTaskClassList = mutableListOf<Class<out IBaseTask>>()

        // 已经执行完成的Task Class列表
        private val mCompleteTaskClassList = mutableListOf<Class<out IBaseTask>>()

        // 在主线程中执行的Task列表
        private val mRunMainTaskList = mutableListOf<BaseTask>()
    }

    /**
     * 添加任务
     *
     * @param task 任务
     * @return
     */
    fun addTask(task: BaseTask?): AppStartUtil {
        if (task == null) {
            return this
        }

        // 解析依赖关系
        initDependedTask(task)
        mAllTaskList.add(task)
        mAllTaskClassList.add(task.javaClass)

        if (isNeedWait(task) && !isInvolvesPrivacy(task)) {
            mMainNeedWaitTaskCount.getAndIncrement()
        }
        if (isInvolvesPrivacy(task)) {
            mInvolvesPrivacyTaskCount.getAndIncrement()
        }
        return this
    }

    /**
     * 开始执行初始化任务
     */
    fun startTask() {
        executor = ThreadPoolExecutor(
            getCorePoolSize(),
            getCorePoolSize(),
            5L,
            TimeUnit.SECONDS,
            LinkedBlockingQueue(),
            DefaultThreadFactory()
        ) { r, _ -> Executors.newCachedThreadPool().execute(r) }
        check(Looper.getMainLooper() == Looper.myLooper()) {
            "启动器必须在主线程中执行"
        }

        mAllTaskList.takeIf {
            it.size > 0
        }?.run {
            // 返回拓扑排序后的任务列表(这里也就是通过拓扑算法来计算出每个节点的入度数,从而进行排序)
            mAllTaskList = TaskSortUtils.getSortTask(mAllTaskList, mAllTaskClassList)
            mMainThreadCountDownLatch = CountDownLatch(mMainNeedWaitTaskCount.get())
            mInvolvesPrivacyCountDownLatch = CountDownLatch(mInvolvesPrivacyTaskCount.get())

            dispatchTasks()
            executeMainThread()
        }
    }

    /**
     * 建议Task与它依赖的Task的映射关系
     */
    private fun initDependedTask(task: BaseTask) {
        task.dependentTaskList()?.takeIf {
            !it.isNullOrEmpty()
        }?.forEach {
            // 初始化映射map
            if (mDependTaskHashMap[it] == null) {
                mDependTaskHashMap[it] = mutableListOf()
            }

            // 将该任务添加到依赖类
            mDependTaskHashMap[it]?.add(task)

            // 如果某个依赖类已经执行完成，那么对该Task执行一次解锁
            if (mCompleteTaskClassList.contains(it)) {
                task.stopLock()
            }
        }
    }

    /**
     * 分发任务,将任务都添加到各自的List中去
     */
    private fun dispatchTasks() {
        mAllTaskList.takeIf {
            it.size > 0
        }?.forEach {

            // 判断是否涉及隐私政策
            if (!it.isInvolvesPrivacy()) {
                if (it.isOnMainThread()) {
                    // 主线程执行的任务，加入到主线程任务列表中
                    mRunMainTaskList.add(it)
                } else {
                    // 子线程执行的任务，调用Task指定的线程池执行
                    executor?.submit(TaskRunnable(it, this))
                }
            } else {
                LogUtils.d("------涉及到用户隐私,暂停初始化------", TAG)
                if (it.isOnMainThread()) {
                    mInvolvesRunMainTaskList.add(it)
                } else {
                    mInvolvesPrivacyTaskList.add(it)
                }
            }
        }
    }

    /**
     * 每个任务完成时都会发出通知
     * (1)所有依赖它的任务都执行一次解锁
     * (2)如果主线程需要等待该任务，则该任务完成时，主线程执行一次解锁
     */
    fun notifyTaskFinish(task: BaseTask) {
        mDependTaskHashMap[task.javaClass]?.takeIf {
            it.size > 0
        }?.forEach {
            it.stopLock()
        }
        mCompleteTaskClassList.add(task.javaClass)

        if (isNeedWait(task) && !isInvolvesPrivacy(task)) {
            mMainThreadCountDownLatch?.countDown()
            mMainNeedWaitTaskCount.getAndDecrement()
            LogUtils.i("任务 --> ${task::class.java.simpleName}完成,主线程剩余等待依赖数：${mMainNeedWaitTaskCount.get()}",
                TAG
            )
        }
        if (isInvolvesPrivacy(task)) {
            mInvolvesPrivacyCountDownLatch?.countDown()
            mInvolvesPrivacyTaskCount.getAndDecrement()
            LogUtils.i("任务 --> ${task::class.java.simpleName}完成,涉及隐私剩余需要延迟的任务个数：${mInvolvesPrivacyTaskCount.get()}",
                TAG
            )
        }
    }

    /**
     * 判断主线程是否等待子线程Task执行完成
     * 部分子线程的任务需要执行完成后主线程才能继续执行
     *
     * @param task
     * @return
     */
    private fun isNeedWait(task: BaseTask): Boolean = task.isOnMainThread()

    /**
     * 判断当前任务是否涉及到隐私,需要延迟初始化
     *
     * @param task
     * @return
     */
    private fun isInvolvesPrivacy(task: BaseTask): Boolean = task.isInvolvesPrivacy()

    /**
     * 执行主线程任务
     */
    private fun executeMainThread() {
        mRunMainTaskList.takeIf {
            it.size > 0
        }?.forEach {
            LogUtils.i("开始执行普通主线程任务", TAG)
            TaskRunnable(it, this).run()
        }
    }

    /**
     * 获取核心线程数
     */
    private fun getCorePoolSize(): Int {
        val corePoolSize =
            2.coerceAtLeast((Runtime.getRuntime().availableProcessors() - 1).coerceAtMost(5))
        LogUtils.i("获取到的核心线程个数 -> $corePoolSize", TAG)
        return corePoolSize
    }

    /**
     * 给主线程上锁
     * 当需要主线程等待的任务完成时，计数-1
     */
    fun startLockMainThread() {
        try {
            LogUtils.d("-----主线程上锁-----", TAG)
            if (mMainNeedWaitTaskCount.get() > 0) {
                LogUtils.d("主线程等待依赖数：${mMainNeedWaitTaskCount.get()}", TAG)
                mMainThreadCountDownLatch?.await(
                    MAIN_THREAD_WAIT_MILLIONS_TIME,
                    TimeUnit.MILLISECONDS
                )
            } else {
                LogUtils.d("-----主线程解锁-----", TAG)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 开始执行延迟任务,但不锁定线程,主要用于已经同意隐私政策之后的初始化
     *
     */
    fun startInvolvesPrivacyTaskNoAwait() {
        LogUtils.d("-----开始执行延迟任务-----", TAG)
        mInvolvesPrivacyTaskList.forEach {
            // 子线程执行的任务，调用Task指定的线程池执行
            executor?.submit(TaskRunnable(it, this))
        }
        LogUtils.i(">>开始执行延迟任务之-主线程任务<<", TAG)
        mInvolvesRunMainTaskList.forEach {
            TaskRunnable(it, this).run()
        }
    }

    /**
     * 开始执行延迟任务,锁定当前线程,等待任务完成
     *
     */
    fun startInvolvesPrivacyTask() {
        try {
            LogUtils.d("-----开始执行延迟任务-----", TAG)
            mInvolvesPrivacyTaskList.forEach {
                // 子线程执行的任务，调用Task指定的线程池执行
                executor?.submit(TaskRunnable(it, this))
            }
            LogUtils.i(">>开始执行延迟任务之-主线程任务<<", TAG)
            mInvolvesRunMainTaskList.forEach {
                TaskRunnable(it, this).run()
            }
            if (mInvolvesPrivacyTaskCount.get() > 0) {
                LogUtils.d("主线程等待依赖数：${mInvolvesPrivacyTaskCount.get()}", TAG)
                mInvolvesPrivacyCountDownLatch?.await(
                    MAIN_THREAD_WAIT_MILLIONS_TIME,
                    TimeUnit.MILLISECONDS
                )
            } else {
                LogUtils.w("------延迟任务执行完毕-----", TAG)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 默认线程工厂
     */
    private class DefaultThreadFactory : ThreadFactory {
        // 线程池个数
        private val poolNumber: AtomicInteger = AtomicInteger(1)

        // 线程个数
        private var threadNumber: AtomicInteger = AtomicInteger(1)

        private var group: ThreadGroup?
        private var threadNamePrex: String = ""

        init {
            val securityManager: SecurityManager? = System.getSecurityManager()
            group = securityManager?.threadGroup ?: Thread.currentThread().threadGroup
            threadNamePrex =
                "${AppStartUtil::class.java.simpleName}-${poolNumber.getAndIncrement()}-Thread-"
        }

        /**
         * 自定义创建线程
         *
         * @param r
         * @return
         */
        override fun newThread(r: Runnable?): Thread {
            LogUtils.i("创建新线程,线程名为：$threadNamePrex${threadNumber.getAndIncrement()}", TAG)
            val thread = Thread(group, r, "$threadNamePrex${threadNumber.getAndIncrement()}", 0)
            if (thread.isDaemon) {
                thread.isDaemon = false
            }

            if (thread.priority != Thread.NORM_PRIORITY) {
                thread.priority = Thread.NORM_PRIORITY
            }
            return thread
        }
    }

}