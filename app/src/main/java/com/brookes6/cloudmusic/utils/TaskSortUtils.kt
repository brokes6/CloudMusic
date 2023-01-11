package com.brookes6.cloudmusic.utils

import android.text.TextUtils
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.GraphSort
import com.brookes6.cloudmusic.launch.IBaseTask

/**
 * Mender:
 * Modify:
 * Description: 任务排序工具类
 */
internal object TaskSortUtils {

    private var TAG = TaskSortUtils::class.java.simpleName

    // 排序后Task列表
    private var mTaskList: MutableList<IBaseTask> = mutableListOf()

    /**
     * 获取拓扑排序后的Task列表
     */
    fun getSortTask(
        originTasks: MutableList<BaseTask>,
        originTaskClass: MutableList<Class<out IBaseTask>>
    ): MutableList<BaseTask> {
        val dependedTaskIndexList = mutableListOf<Int>()
        val graph = GraphSort(originTasks.size)

        // 遍历构成相邻链表
        originTasks.forEachIndexed { index, task ->
            if (task.dependentTaskList().isNullOrEmpty()) {
                return@forEachIndexed
            }

            task.dependentTaskList()?.forEach { clz ->
                val indexOfTaskClass: Int = getIndexOfTaskList(originTasks, originTaskClass, clz)
                check(indexOfTaskClass >= 0) {
                    "${task.javaClass.simpleName}对应依赖${clz}未找到"
                }
                // 查找所有被依赖的Task，便于后面先执行被依赖的Task
                dependedTaskIndexList.add(indexOfTaskClass)
                graph.addEdge(indexOfTaskClass, index)
            }
        }
        // 索引的拓扑排序
        val sortIndexList = graph.topologySort()
        // 将拓扑排序转化为实际的任务列表
        return transFormSortTask(originTasks, dependedTaskIndexList, sortIndexList)
    }

    /**
     * 将拓扑索引结果转化为Task列表
     *
     */
    private fun transFormSortTask(
        originTasks: MutableList<BaseTask>,
        dependedTaskIndexList: MutableList<Int>,
        sortIndexList: MutableList<Int>
    ): MutableList<BaseTask> {
        // Task结果列表
        val resultTasks = mutableListOf<BaseTask>()
        // 被其他Task依赖的Task列表
        val dependedTasks = mutableListOf<BaseTask>()
        // 需要提高执行优先级的Tasks列表
        val runImmediatelyTasks = mutableListOf<BaseTask>()
        // 不存在依赖关系的任务
        val withoutDependedTasks = mutableListOf<BaseTask>()

        // 对拓扑排序后的任务进行排序
        // 执行顺序为：被依赖的任务-> 优先级高的任务->需要被等待的任务(主线程)->没有依赖的任务
        val builder = StringBuilder()
        sortIndexList.forEach {
            builder.append(it).append("-")
            if (dependedTaskIndexList.contains(it)) {
                dependedTasks.add(originTasks[it])
            } else {
                originTasks[it].let { task ->
                    if (task.isRunImmediately()) {
                        runImmediatelyTasks.add(task)
                    } else {
                        withoutDependedTasks.add(task)
                    }
                }
            }
        }
        LogUtils.d("启动器拓扑排序后为：${builder}", TAG)
        resultTasks.addAll(dependedTasks)
        resultTasks.addAll(runImmediatelyTasks)
        resultTasks.addAll(withoutDependedTasks)
        return resultTasks
    }

    /**
     * 获取当前Task的class对应在列表中的索引
     */
    private fun getIndexOfTaskList(
        originTasks: MutableList<BaseTask>,
        originTaskClass: MutableList<Class<out IBaseTask>>,
        clazz: Class<out IBaseTask>
    ): Int {
        var index: Int
        index = originTaskClass.indexOf(clazz)
        if (index >= 0) {
            return index
        }

        originTasks.forEachIndexed { i, task ->
            if (TextUtils.equals(task.javaClass.simpleName, clazz.simpleName)) {
                index = i
            }
        }
        return index
    }

}