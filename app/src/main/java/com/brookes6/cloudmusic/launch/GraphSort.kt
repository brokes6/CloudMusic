package com.brookes6.cloudmusic.launch

import java.util.*

/**
 * 通过拓扑算法来将每个Task任务进行排序
 *
 * @property mNodeCount 结点个数
 */
internal class GraphSort(
    var mNodeCount: Int // 图中结点个数
) {

    // 邻接链表
    private var mAdj: MutableList<MutableList<Int>> = MutableList(mNodeCount) {
        mutableListOf<Int>()
    }

    /**
     * 添加边
     * 从u->v的边
     * @param u 边的尾部
     * @param v 边的头部
     */
    fun addEdge(u: Int, v: Int) {
        mAdj[u].add(v)
    }

    /**
     * 进行拓扑排序
     */
    fun topologySort(): Vector<Int> {
        // 记录所有结点的入度数，数组索引对应的即为结点值，而数组的值对应的是入度数
        val degreeArray = MutableList<Int>(mNodeCount) {
            0
        }

        // 初始化所有结点的入度数
        for (index in 0 until mNodeCount) {
            mAdj[index].takeIf {
                it.size > 0
            }?.forEachIndexed { _, i ->
                degreeArray[i]++
            }
        }

        val queue = LinkedList<Int>()
        // 找出当前所有入度为0的节点
        degreeArray.forEachIndexed { index, i ->
            if (i == 0) {
                queue.add(index)
            }
        }

        var validCount: Int = 0
        val topOrder = Vector<Int>()
        while (!queue.isNullOrEmpty()) {
            val u: Int = queue.poll() ?: 0
            topOrder.add(u)
            mAdj[u].forEach {
                if (--degreeArray[it] == 0) {
                    queue.add(it)
                }
            }
            validCount++
        }
        // 节点数与入度为0的所有结点个数不等，则一定存在环
        check(validCount == mNodeCount) {
            "拓扑排序不能存在环"
        }

        return topOrder
    }
}