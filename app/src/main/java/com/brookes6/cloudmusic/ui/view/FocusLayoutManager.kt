package com.brookes6.cloudmusic.ui.view

import android.animation.ValueAnimator
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.View.FOCUS_LEFT
import android.view.animation.LinearInterpolator
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.LogUtils.e
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * Created by ccy(17022) on 2019/5/18 下午5:07
 */
class FocusLayoutManager private constructor(builder: Builder) : RecyclerView.LayoutManager() {
    /**
     * 最大可堆叠层级
     */
    private var maxLayerCount: Int = 3
        set(value) {
            field = value
            resetData()
            requestLayout()
        }

    /**
     * 堆叠的方向。
     * 期望滚动方向为水平时，传[.FOCUS_LEFT]或[.FOCUS_RIGHT]；
     * 期望滚动方向为垂直时，传[.FOCUS_TOP]或[.FOCUS_BOTTOM]。
     */
    @FocusOrientation
    private var focusOrientation = FOCUS_LEFT

    /**
     * 堆叠view之间的偏移量
     */
    private var layerPadding: Float

    /**
     * 普通view之间的margin
     */
    private var normalViewGap: Float

    /**
     * 是否自动选中
     */
    var isAutoSelect: Boolean

    /**
     * 变换监听接口。
     */
    private val trasitionListeners: MutableList<TrasitionListener?>?

    /**
     *
     */
    var onFocusChangeListener: OnFocusChangeListener?

    /**
     * 水平方向累计偏移量
     */
    private var mHorizontalOffset: Long = 0

    /**
     * 垂直方向累计偏移量
     */
    private var mVerticalOffset: Long = 0

    /**
     * 屏幕可见的第一个View的Position
     */
    private var mFirstVisiPos = 0

    /**
     * 屏幕可见的最后一个View的Position
     */
    private var mLastVisiPos = 0

    /**
     * 一次完整的聚焦滑动所需要移动的距离。
     */
    private var onceCompleteScrollLength = -1f

    /**
     * 焦点view的position
     */
    var focusdPosition = -1
        private set

    /**
     * 自动选中动画
     */
    private var selectAnimator: ValueAnimator? = null
    private val autoSelectMinDuration: Long
    private val autoSelectMaxDuration: Long

    @IntDef(*[FOCUS_LEFT, FOCUS_RIGHT, FOCUS_TOP, FOCUS_BOTTOM])
    @Retention(RetentionPolicy.SOURCE)
    annotation class FocusOrientation {}

    constructor() : this(Builder()) {}

    init {
        maxLayerCount = builder.maxLayerCount
        focusOrientation = builder.focusOrientation
        layerPadding = builder.layerPadding
        trasitionListeners = builder.trasitionListeners
        normalViewGap = builder.normalViewGap
        isAutoSelect = builder.isAutoSelect
        onFocusChangeListener = builder.onFocusChangeListener
        autoSelectMinDuration = builder.autoSelectMinDuration
        autoSelectMaxDuration = builder.autoSelectMaxDuration
    }

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.WRAP_CONTENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        if (state.itemCount == 0) {
            removeAndRecycleAllViews(recycler)
            return
        }
        onceCompleteScrollLength = -1f
        //分离全部已有的view，放入临时缓存
        detachAndScrapAttachedViews(recycler)
        fill(recycler, state, 0)
    }

    override fun canScrollHorizontally(): Boolean {
        return focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT
    }

    override fun canScrollVertically(): Boolean {
        return focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM
    }

    override fun scrollHorizontallyBy(
        dx: Int, recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        //手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;

        //位移0、没有子View 当然不移动
        var dx = dx
        if (dx == 0 || childCount == 0) {
            return 0
        }
        mHorizontalOffset += dx.toLong() //累加实际滑动距离
        dx = fill(recycler, state, dx)
        return dx
    }

    override fun scrollVerticallyBy(
        dy: Int, recycler: Recycler,
        state: RecyclerView.State
    ): Int {
        //位移0、没有子View 当然不移动
        var dy = dy
        if (dy == 0 || childCount == 0) {
            return 0
        }
        mVerticalOffset += dy.toLong() //累加实际滑动距离
        dy = fill(recycler, state, dy)
        return dy
    }

    override fun onDetachedFromWindow(view: RecyclerView, recycler: Recycler) {
        cancelAnimator()
        super.onDetachedFromWindow(view, recycler)
    }

    /**
     * @param recycler
     * @param state
     * @param delta
     */
    private fun fill(recycler: Recycler, state: RecyclerView.State, delta: Int): Int {
        var resultDelta = delta
        when (focusOrientation) {
            FOCUS_LEFT -> resultDelta = fillHorizontalLeft(recycler, state, delta)
            FOCUS_RIGHT -> resultDelta = fillHorizontalRight(recycler, state, delta)
            FOCUS_TOP -> resultDelta = fillVerticalTop(recycler, state, delta)
            FOCUS_BOTTOM -> resultDelta = fillVerticalBottom(recycler, state, delta)
            else -> {}
        }
        recycleChildren(recycler)
        return resultDelta
    }

    /**
     * 水平滚动、向左堆叠布局
     *
     * @param recycler
     * @param state
     * @param dx       偏移量。手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
     */
    private fun fillHorizontalLeft(
        recycler: Recycler, state: RecyclerView.State,
        dx: Int
    ): Int {
        //----------------1、边界检测-----------------
        var dx = dx
        if (dx < 0) {
            //已达左边界
            if (mHorizontalOffset < 0) {
                dx = 0
                mHorizontalOffset = dx.toLong()
            }
        }
        if (dx > 0) {
            //滑动到只剩堆叠view，没有普通view了，说明已经到达右边界了
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                //因为scrollHorizontallyBy里加了一次dx，现在减回去
                mHorizontalOffset -= dx.toLong()
                dx = 0
            }
        }

        //分离全部的view，放入临时缓存
        detachAndScrapAttachedViews(recycler)

        //----------------2、初始化布局数据-----------------
        var startX = paddingLeft - layerPadding
        var tempView: View? = null
        var tempPosition = -1
        if (onceCompleteScrollLength == -1f) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView) + normalViewGap
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        val fraction =
            Math.abs(mHorizontalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        val layerViewOffset = layerPadding * fraction
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        val normalViewOffset = onceCompleteScrollLength * fraction
        var isLayerViewOffsetSetted = false
        var isNormalViewOffsetSetted = false

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos =
            floor((abs(mHorizontalOffset) / onceCompleteScrollLength).toDouble()).toInt() //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = itemCount - 1

        val newFocusedPosition = mFirstVisiPos + maxLayerCount - 1
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener!!.onFocusChanged(newFocusedPosition, focusdPosition)
            }
            focusdPosition = newFocusedPosition
        }

        //----------------3、开始布局-----------------
        for (i in mFirstVisiPos..mLastVisiPos) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                val item: View = if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    tempView
                } else {
                    recycler.getViewForPosition(i)
                }
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startX += layerPadding
                if (!isLayerViewOffsetSetted) {
                    startX -= layerViewOffset
                    isLayerViewOffsetSetted = true
                }
                if (trasitionListeners != null && trasitionListeners.isNotEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        trasitionListener!!.handleLayerView(
                            this, item, i - mFirstVisiPos,
                            maxLayerCount, i, fraction, dx.toFloat()
                        )
                    }
                }
                val l: Int = startX.toInt()
                val t: Int = paddingTop
                val r: Int = (startX + getDecoratedMeasurementHorizontal(item)).toInt()
                val b: Int = paddingTop + getDecoratedMeasurementVertical(item)
                layoutDecoratedWithMargins(item, l, t, r, b)
            } else { //属于普通区域
                val item = recycler.getViewForPosition(i)
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startX += onceCompleteScrollLength
                if (!isNormalViewOffsetSetted) {
                    startX += layerViewOffset
                    startX -= normalViewOffset
                    isNormalViewOffsetSetted = true
                }
                if (trasitionListeners != null && trasitionListeners.isNotEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener!!.handleFocusingView(
                                this,
                                item,
                                i,
                                fraction,
                                dx.toFloat()
                            )
                        } else {
                            trasitionListener!!.handleNormalView(
                                this,
                                item,
                                i,
                                fraction,
                                dx.toFloat()
                            )
                        }
                    }
                }
                val l: Int = startX.toInt()
                val t: Int = paddingTop
                val r: Int = (startX + getDecoratedMeasurementHorizontal(item)).toInt()
                val b: Int = paddingTop + getDecoratedMeasurementVertical(item)
                layoutDecoratedWithMargins(item, l, t, r, b)

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startX + onceCompleteScrollLength > width - paddingRight) {
                    mLastVisiPos = i
                    break
                }
            }
        }
        return dx
    }

    /**
     * 水平滚动、向右堆叠布局。详细注释请参考
     * [.fillHorizontalLeft]。
     *
     * @param recycler
     * @param state
     * @param dx       偏移量。手指从右向左滑动，dx > 0; 手指从左向右滑动，dx < 0;
     */
    private fun fillHorizontalRight(
        recycler: Recycler, state: RecyclerView.State,
        dx: Int
    ): Int {

        //----------------1、边界检测-----------------

        //从最右边开始布局，所以mHorizontalOffset一直是负数
        var dx = dx
        if (dx > 0) {
            if (mHorizontalOffset > 0) {
                dx = 0
                mHorizontalOffset = dx.toLong()
            }
        }
        if (dx < 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mHorizontalOffset -= dx.toLong()
                dx = 0
            }
        }
        detachAndScrapAttachedViews(recycler)

        //----------------2、初始化布局数据-----------------
        var startX = width - paddingRight + layerPadding
        var tempView: View? = null
        var tempPosition = -1
        if (onceCompleteScrollLength == -1f) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            onceCompleteScrollLength = getDecoratedMeasurementHorizontal(tempView) + normalViewGap
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        val fraction =
            Math.abs(mHorizontalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        val layerViewOffset = layerPadding * fraction
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        val normalViewOffset = onceCompleteScrollLength * fraction
        var isLayerViewOffsetSetted = false
        var isNormalViewOffsetSetted = false

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos =
            Math.floor((Math.abs(mHorizontalOffset) / onceCompleteScrollLength).toDouble())
                .toInt() //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = itemCount - 1
        val newFocusedPosition = mFirstVisiPos + maxLayerCount - 1
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener!!.onFocusChanged(newFocusedPosition, focusdPosition)
            }
            focusdPosition = newFocusedPosition
        }


        //----------------3、开始布局-----------------
        for (i in mFirstVisiPos..mLastVisiPos) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                val item: View = if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    tempView
                } else {
                    recycler.getViewForPosition(i)
                }
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startX -= layerPadding
                if (!isLayerViewOffsetSetted) {
                    startX += layerViewOffset
                    isLayerViewOffsetSetted = true
                }
                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        trasitionListener!!.handleLayerView(
                            this, item, i - mFirstVisiPos,
                            maxLayerCount, i, fraction, dx.toFloat()
                        )
                    }
                }
                val l: Int = (startX - getDecoratedMeasurementHorizontal(item)).toInt()
                val t: Int = paddingTop
                val r: Int = startX.toInt()
                val b: Int = paddingTop + getDecoratedMeasurementVertical(item)
                layoutDecoratedWithMargins(item, l, t, r, b)
            } else { //属于普通区域
                val item = recycler.getViewForPosition(i)
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startX -= onceCompleteScrollLength
                if (!isNormalViewOffsetSetted) {
                    startX -= layerViewOffset
                    startX += normalViewOffset
                    isNormalViewOffsetSetted = true
                }
                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener!!.handleFocusingView(
                                this,
                                item,
                                i,
                                fraction,
                                dx.toFloat()
                            )
                        } else {
                            trasitionListener!!.handleNormalView(
                                this,
                                item,
                                i,
                                fraction,
                                dx.toFloat()
                            )
                        }
                    }
                }
                val l: Int = (startX - getDecoratedMeasurementHorizontal(item)).toInt()
                val t: Int = paddingTop
                val r: Int = startX.toInt()
                val b: Int = paddingTop + getDecoratedMeasurementVertical(item)
                layoutDecoratedWithMargins(item, l, t, r, b)

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startX - onceCompleteScrollLength < paddingLeft) {
                    mLastVisiPos = i
                    break
                }
            }
        }
        return dx
    }

    /**
     * 垂直滚动、向上堆叠布局
     * 详细注释请参考[.fillHorizontalLeft]。
     *
     * @param recycler
     * @param state
     * @param dy       偏移量。手指从上向下滑动，dy < 0; 手指从下向上滑动，dy > 0;
     */
    private fun fillVerticalTop(
        recycler: Recycler, state: RecyclerView.State,
        dy: Int
    ): Int {

        //----------------1、边界检测-----------------
        var dy = dy
        if (dy < 0) {
            if (mVerticalOffset < 0) {
                dy = 0
                mVerticalOffset = dy.toLong()
            }
        }
        if (dy > 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mVerticalOffset -= dy.toLong()
                dy = 0
            }
        }
        detachAndScrapAttachedViews(recycler)

        //----------------2、初始化布局数据-----------------
        var startY = paddingTop - layerPadding
        var tempView: View? = null
        var tempPosition = -1
        if (onceCompleteScrollLength == -1f) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            onceCompleteScrollLength = getDecoratedMeasurementVertical(tempView) + normalViewGap
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        val fraction =
            Math.abs(mVerticalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        val layerViewOffset = layerPadding * fraction
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        val normalViewOffset = onceCompleteScrollLength * fraction
        var isLayerViewOffsetSetted = false
        var isNormalViewOffsetSetted = false

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos =
            Math.floor((Math.abs(mVerticalOffset) / onceCompleteScrollLength).toDouble()).toInt()
        //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = itemCount - 1
        val newFocusedPosition = mFirstVisiPos + maxLayerCount - 1
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener!!.onFocusChanged(newFocusedPosition, focusdPosition)
            }
            focusdPosition = newFocusedPosition
        }


        //----------------3、开始布局-----------------
        for (i in mFirstVisiPos..mLastVisiPos) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                val item: View = if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    tempView
                } else {
                    recycler.getViewForPosition(i)
                }
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startY += layerPadding
                if (!isLayerViewOffsetSetted) {
                    startY -= layerViewOffset
                    isLayerViewOffsetSetted = true
                }
                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        trasitionListener!!.handleLayerView(
                            this, item, i - mFirstVisiPos,
                            maxLayerCount, i, fraction, dy.toFloat()
                        )
                    }
                }
                val l: Int = paddingLeft
                val t: Int = startY.toInt()
                val r: Int = paddingLeft + getDecoratedMeasurementHorizontal(item)
                val b: Int = (startY + getDecoratedMeasurementVertical(item)).toInt()
                layoutDecoratedWithMargins(item, l, t, r, b)
            } else { //属于普通区域
                val item = recycler.getViewForPosition(i)
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startY += onceCompleteScrollLength
                if (!isNormalViewOffsetSetted) {
                    startY += layerViewOffset
                    startY -= normalViewOffset
                    isNormalViewOffsetSetted = true
                }
                if (trasitionListeners != null && trasitionListeners.isNotEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener!!.handleFocusingView(
                                this,
                                item,
                                i,
                                fraction,
                                dy.toFloat()
                            )
                        } else {
                            trasitionListener!!.handleNormalView(
                                this,
                                item,
                                i,
                                fraction,
                                dy.toFloat()
                            )
                        }
                    }
                }
                val l: Int = paddingLeft
                val t: Int = startY.toInt()
                val r: Int = paddingLeft + getDecoratedMeasurementHorizontal(item)
                val b: Int = (startY + getDecoratedMeasurementVertical(item)).toInt()
                layoutDecoratedWithMargins(item, l, t, r, b)

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startY + onceCompleteScrollLength > height - paddingBottom) {
                    mLastVisiPos = i
                    break
                }
            }
        }
        return dy
    }

    /**
     * 垂直滚动、向下堆叠布局
     * 详细注释请参考[.fillHorizontalLeft]。
     *
     * @param recycler
     * @param state
     * @param dy       偏移量。手指从上向下滑动，dy < 0; 手指从下向上滑动，dy > 0;
     */
    private fun fillVerticalBottom(
        recycler: Recycler, state: RecyclerView.State,
        dy: Int
    ): Int {

        //----------------1、边界检测-----------------
        //从最下边开始布局，所以mVerticalOffset一直是负数
        var dy = dy
        if (dy > 0) {
            //已达上边界
            if (mVerticalOffset > 0) {
                dy = 0
                mVerticalOffset = dy.toLong()
            }
        }
        if (dy < 0) {
            if (mLastVisiPos - mFirstVisiPos <= maxLayerCount - 1) {
                mVerticalOffset -= dy.toLong()
                dy = 0
            }
        }
        detachAndScrapAttachedViews(recycler)

        //----------------2、初始化布局数据-----------------
        var startY = height - paddingBottom + layerPadding
        var tempView: View? = null
        var tempPosition = -1
        if (onceCompleteScrollLength == -1f) {
            //因为mFirstVisiPos在下面可能会被改变，所以用tempPosition暂存一下。
            tempPosition = mFirstVisiPos
            tempView = recycler.getViewForPosition(tempPosition)
            measureChildWithMargins(tempView, 0, 0)
            onceCompleteScrollLength = getDecoratedMeasurementVertical(tempView) + normalViewGap
        }
        //当前"一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT，从右向左移动fraction将从0%到100%）
        val fraction =
            abs(mVerticalOffset) % onceCompleteScrollLength / (onceCompleteScrollLength * 1.0f)

        //堆叠区域view偏移量。在一次完整的聚焦滑动期间，其总偏移量是一个layerPadding的距离
        val layerViewOffset = layerPadding * fraction
        //普通区域view偏移量。在一次完整的聚焦滑动期间，其总位移量是一个onceCompleteScrollLength
        val normalViewOffset = onceCompleteScrollLength * fraction
        var isLayerViewOffsetSetted = false
        var isNormalViewOffsetSetted = false

        //修正第一个可见的view：mFirstVisiPos。已经滑动了多少个完整的onceCompleteScrollLength就代表滑动了多少个item
        mFirstVisiPos =
            floor((abs(mVerticalOffset) / onceCompleteScrollLength).toDouble()).toInt()
        //向下取整
        //临时将mLastVisiPos赋值为getItemCount() - 1，放心，下面遍历时会判断view是否已溢出屏幕，并及时修正该值并结束布局
        mLastVisiPos = itemCount - 1
        val newFocusedPosition = mFirstVisiPos + maxLayerCount - 1
        if (newFocusedPosition != focusdPosition) {
            if (onFocusChangeListener != null) {
                onFocusChangeListener!!.onFocusChanged(newFocusedPosition, focusdPosition)
            }
            focusdPosition = newFocusedPosition
        }


        //----------------3、开始布局-----------------
        for (i in mFirstVisiPos..mLastVisiPos) {
            //属于堆叠区域
            if (i - mFirstVisiPos < maxLayerCount) {
                val item: View = if (i == tempPosition && tempView != null) {
                    //如果初始化数据时已经取了一个临时view，可别浪费了！
                    tempView
                } else {
                    recycler.getViewForPosition(i)
                }
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startY -= layerPadding
                if (!isLayerViewOffsetSetted) {
                    startY += layerViewOffset
                    isLayerViewOffsetSetted = true
                }
                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        trasitionListener!!.handleLayerView(
                            this, item, i - mFirstVisiPos,
                            maxLayerCount, i, fraction, dy.toFloat()
                        )
                    }
                }
                val l: Int = paddingLeft
                val t: Int = (startY - getDecoratedMeasurementVertical(item)).toInt()
                val r: Int = paddingLeft + getDecoratedMeasurementHorizontal(item)
                val b: Int = startY.toInt()
                layoutDecoratedWithMargins(item, l, t, r, b)
            } else { //属于普通区域
                val item = recycler.getViewForPosition(i)
                addView(item)
                measureChildWithMargins(item, 0, 0)
                startY -= onceCompleteScrollLength
                if (!isNormalViewOffsetSetted) {
                    startY -= layerViewOffset
                    startY += normalViewOffset
                    isNormalViewOffsetSetted = true
                }
                if (trasitionListeners != null && !trasitionListeners.isEmpty()) {
                    for (trasitionListener in trasitionListeners) {
                        if (i - mFirstVisiPos == maxLayerCount) {
                            trasitionListener!!.handleFocusingView(
                                this,
                                item,
                                i,
                                fraction,
                                dy.toFloat()
                            )
                        } else {
                            trasitionListener!!.handleNormalView(
                                this,
                                item,
                                i,
                                fraction,
                                dy.toFloat()
                            )
                        }
                    }
                }
                var l: Int
                var t: Int
                var r: Int
                var b: Int
                l = paddingLeft
                t = (startY - getDecoratedMeasurementVertical(item)).toInt()
                r = paddingLeft + getDecoratedMeasurementHorizontal(item)
                b = startY.toInt()
                layoutDecoratedWithMargins(item, l, t, r, b)

                //判断下一个view的布局位置是不是已经超出屏幕了，若超出，修正mLastVisiPos并跳出遍历
                if (startY - onceCompleteScrollLength < paddingTop) {
                    mLastVisiPos = i
                    break
                }
            }
        }
        return dy
    }

    override fun onAdapterChanged(
        oldAdapter: RecyclerView.Adapter<*>?,
        newAdapter: RecyclerView.Adapter<*>?
    ) {
        resetData()
        super.onAdapterChanged(oldAdapter, newAdapter)
    }

    override fun onMeasure(
        recycler: Recycler,
        state: RecyclerView.State, widthSpec: Int, heightSpec: Int
    ) {
        val widthMode = View.MeasureSpec.getMode(widthSpec)
        val heightMode = View.MeasureSpec.getMode(heightSpec)
        if (widthMode == View.MeasureSpec.AT_MOST && (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT)) {
            e(
                "FocusLayoutManager-onMeasure:当滚动方向为水平时，recyclerView的宽度请不要使用wrap_content",
                "FocusLayoutManager"
            )
        }
        super.onMeasure(recycler, state, widthSpec, heightSpec)
    }

    override fun isAutoMeasureEnabled(): Boolean {
        return true
    }

    /**
     * 回收需回收的Item。
     */
    private fun recycleChildren(recycler: Recycler) {
        val scrapList = recycler.scrapList
        for (i in scrapList.indices) {
            val holder = scrapList[i]
            removeAndRecycleView(holder.itemView, recycler)
        }
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        when (state) {
            RecyclerView.SCROLL_STATE_DRAGGING ->                 //当手指按下时，停止当前正在播放的动画
                cancelAnimator()
            RecyclerView.SCROLL_STATE_IDLE ->                 //当列表滚动停止后，判断一下自动选中是否打开
                if (isAutoSelect) {
                    //找到离目标落点最近的item索引
                    smoothScrollToPosition(findShouldSelectPosition())
                }
            else -> {}
        }
    }

    /**
     * 返回应当选中的position
     *
     * @return
     */
    private fun findShouldSelectPosition(): Int {
        if (onceCompleteScrollLength == -1f || mFirstVisiPos == -1) {
            return -1
        }
        var remainder = -1
        if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
            remainder = (Math.abs(mHorizontalOffset) % onceCompleteScrollLength).toInt()
        } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
            remainder = (Math.abs(mVerticalOffset) % onceCompleteScrollLength).toInt()
        }
        if (remainder >= onceCompleteScrollLength / 2.0f) { //超过一半，应当选中下一项
            if (mFirstVisiPos + 1 <= itemCount - 1) {
                return mFirstVisiPos + 1
            }
        }
        return mFirstVisiPos
    }

    /**
     * 返回移动到position所需的偏移量
     *
     * @param position
     * @return
     */
    private fun getScrollToPositionOffset(position: Int): Float {
        when (focusOrientation) {
            FOCUS_LEFT -> {
                return position * onceCompleteScrollLength - Math.abs(mHorizontalOffset)
            }
            FOCUS_RIGHT -> {
                return -(position * onceCompleteScrollLength - Math.abs(mHorizontalOffset))
            }
            FOCUS_TOP -> {
                return position * onceCompleteScrollLength - Math.abs(mVerticalOffset)
            }
            FOCUS_BOTTOM -> {
                return -(position * onceCompleteScrollLength - Math.abs(mVerticalOffset))
            }
            else -> return 0f
        }
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView, state: RecyclerView.State,
        position: Int
    ) {
        smoothScrollToPosition(position)
    }

    override fun scrollToPosition(position: Int) {
        if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
            mHorizontalOffset += getScrollToPositionOffset(position).toLong()
            requestLayout()
        } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
            mVerticalOffset += getScrollToPositionOffset(position).toLong()
            requestLayout()
        }
    }

    /**
     * 平滑滚动到某个位置
     *
     * @param position 目标Item索引
     */
    fun smoothScrollToPosition(position: Int) {
        if (position > -1 && position < itemCount) {
            startValueAnimator(position)
        }
    }

    private fun startValueAnimator(position: Int) {
        cancelAnimator()
        val distance = getScrollToPositionOffset(position)
        val minDuration = autoSelectMinDuration
        val maxDuration = autoSelectMaxDuration
        val duration: Long
        val distanceFraction = Math.abs(distance) / onceCompleteScrollLength
        duration = if (distance <= onceCompleteScrollLength) {
            (minDuration + (maxDuration - minDuration) * distanceFraction).toLong()
        } else {
            (maxDuration * distanceFraction).toLong()
        }
        selectAnimator = ValueAnimator.ofFloat(0.0f, distance).apply {
            setDuration(duration)
            interpolator = LinearInterpolator()
            val startedOffset =
                if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) mHorizontalOffset.toFloat() else mVerticalOffset.toFloat()
            addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation ->
                if (focusOrientation == FOCUS_LEFT || focusOrientation == FOCUS_RIGHT) {
                    mHorizontalOffset = if (mHorizontalOffset < 0) {
                        floor((startedOffset + animation.animatedValue as Float).toDouble())
                            .toLong()
                    } else {
                        ceil((startedOffset + animation.animatedValue as Float).toDouble())
                            .toLong()
                    }
                    requestLayout()
                } else if (focusOrientation == FOCUS_TOP || focusOrientation == FOCUS_BOTTOM) {
                    mVerticalOffset = if (mVerticalOffset < 0) {
                        floor((startedOffset + animation.animatedValue as Float).toDouble())
                            .toLong()
                    } else {
                        ceil((startedOffset + animation.animatedValue as Float).toDouble())
                            .toLong()
                    }
                    requestLayout()
                }
            })
            start()
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间，将margin考虑进去
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementHorizontal(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin)
    }

    /**
     * 获取某个childView在竖直方向所占的空间,将margin考虑进去
     *
     * @param view
     * @return
     */
    fun getDecoratedMeasurementVertical(view: View): Int {
        val params = view.layoutParams as RecyclerView.LayoutParams
        return (getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin)
    }

    val verticalSpace: Int
        get() = height - paddingTop - paddingBottom
    val horizontalSpace: Int
        get() = width - paddingLeft - paddingRight

    /**
     * 重置数据
     */
    fun resetData() {
        mFirstVisiPos = 0
        mLastVisiPos = 0
        onceCompleteScrollLength = -1f
        mHorizontalOffset = 0
        mVerticalOffset = 0
        focusdPosition = -1
        cancelAnimator()
    }

    fun cancelAnimator() {
        if (selectAnimator != null && (selectAnimator!!.isStarted || selectAnimator!!.isRunning)) {
            selectAnimator!!.cancel()
        }
    }

    fun setFocusdPosition(focusdPosition: Int, anim: Boolean) {
        if (focusdPosition > -1 && focusdPosition < itemCount && focusdPosition >= maxLayerCount - 1) {
            if (anim) {
                smoothScrollToPosition(focusdPosition - (maxLayerCount - 1))
            } else {
                scrollToPosition(focusdPosition - (maxLayerCount - 1))
            }
        }
    }

    fun getFocusOrientation(): Int {
        return focusOrientation
    }

    fun setFocusOrientation(@FocusOrientation focusOrientation: Int) {
        this.focusOrientation = focusOrientation
        resetData()
        requestLayout()
    }

    fun getLayerPadding(): Float {
        return layerPadding
    }

    fun setLayerPadding(layerPadding: Float) {
        var layerPadding = layerPadding
        if (layerPadding < 0) {
            layerPadding = 0f
        }
        this.layerPadding = layerPadding
        resetData()
        requestLayout()
    }

    fun getNormalViewGap(): Float {
        return normalViewGap
    }

    fun setNormalViewGap(normalViewGap: Float) {
        this.normalViewGap = normalViewGap
        resetData()
        requestLayout()
    }

    fun getTrasitionListeners(): List<TrasitionListener?>? {
        return trasitionListeners
    }

    fun addTrasitionListener(trasitionListener: TrasitionListener?) {
        trasitionListeners!!.add(trasitionListener)
        requestLayout()
    }

    /**
     * @param trasitionListener if null,remove all
     * @return
     */
    fun removeTrasitionlistener(trasitionListener: TrasitionListener?): Boolean {
        return if (trasitionListener != null) {
            trasitionListeners!!.remove(trasitionListener)
        } else {
            trasitionListeners!!.clear()
            true
        }
    }

    class Builder {
        var maxLayerCount = 3

        @FocusOrientation
        var focusOrientation: Int
        var layerPadding: Float
        var normalViewGap: Float
        var isAutoSelect: Boolean
        val trasitionListeners: MutableList<TrasitionListener?>
        var onFocusChangeListener: OnFocusChangeListener?
        var autoSelectMinDuration: Long
        var autoSelectMaxDuration: Long
        var defaultTrasitionListener: TrasitionListener?

        init {
            focusOrientation = FOCUS_LEFT
            layerPadding = 60f
            normalViewGap = 60f
            isAutoSelect = true
            trasitionListeners = ArrayList()
            defaultTrasitionListener =
                TrasitionListenerConvert(object : SimpleTrasitionListener() {})
            trasitionListeners.add(defaultTrasitionListener)
            onFocusChangeListener = null
            autoSelectMinDuration = 100
            autoSelectMaxDuration = 300
        }

        /**
         * 最大可堆叠层级
         */
        fun maxLayerCount(maxLayerCount: Int): Builder {
            if (maxLayerCount <= 0) {
                throw RuntimeException("maxLayerCount不能小于0")
            }
            this.maxLayerCount = maxLayerCount
            return this
        }

        /**
         * 堆叠的方向。
         * 滚动方向为水平时，传[.FOCUS_LEFT]或[.FOCUS_RIGHT]；
         * 滚动方向为垂直时，传[.FOCUS_TOP]或[.FOCUS_BOTTOM]。
         *
         * @param focusOrientation
         * @return
         */
        fun focusOrientation(@FocusOrientation focusOrientation: Int): Builder {
            this.focusOrientation = focusOrientation
            return this
        }

        /**
         * 堆叠view之间的偏移量
         *
         * @param layerPadding
         * @return
         */
        fun layerPadding(layerPadding: Float): Builder {
            var layerPadding = layerPadding
            if (layerPadding < 0) {
                layerPadding = 0f
            }
            this.layerPadding = layerPadding
            return this
        }

        /**
         * 普通view之间的margin
         */
        fun normalViewGap(normalViewGap: Float): Builder {
            this.normalViewGap = normalViewGap
            return this
        }

        /**
         * 是否自动选中
         */
        fun isAutoSelect(isAutoSelect: Boolean): Builder {
            this.isAutoSelect = isAutoSelect
            return this
        }

        fun autoSelectDuration(
            @IntRange(from = 0) minDuration: Long,
            @IntRange(from = 0) maxDuration: Long
        ): Builder {
            if (minDuration < 0 || maxDuration < 0 || maxDuration < minDuration) {
                throw RuntimeException("autoSelectDuration入参不合法")
            }
            autoSelectMinDuration = minDuration
            autoSelectMaxDuration = maxDuration
            return this
        }

        /**
         * 高级定制 添加滚动过程中view的变换监听接口
         *
         * @param trasitionListener
         * @return
         */
        fun addTrasitionListener(trasitionListener: TrasitionListener?): Builder {
            if (trasitionListener != null) {
                trasitionListeners.add(trasitionListener)
            }
            return this
        }

        /**
         * 简化版 滚动过程中view的变换监听接口。实际会被转换为[TrasitionListener]
         *
         * @param simpleTrasitionListener if null,remove current
         * @return
         */
        fun setSimpleTrasitionListener(simpleTrasitionListener: SimpleTrasitionListener?): Builder {
            trasitionListeners.remove(defaultTrasitionListener)
            defaultTrasitionListener = null
            if (simpleTrasitionListener != null) {
                defaultTrasitionListener = TrasitionListenerConvert(simpleTrasitionListener)
                trasitionListeners.add(defaultTrasitionListener)
            }
            return this
        }

        fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener?): Builder {
            this.onFocusChangeListener = onFocusChangeListener
            return this
        }

        fun build(): FocusLayoutManager {
            return FocusLayoutManager(this)
        }
    }

    /**
     * 滚动过程中view的变换监听接口。属于高级定制，暴露了很多关键布局数据。若定制要求不高，考虑使用[SimpleTrasitionListener]
     */
    interface TrasitionListener {
        /**
         * 处理在堆叠里的view。
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param viewLayer          当前层级，0表示底层，maxLayerCount-1表示顶层
         * @param maxLayerCount      最大层级
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         * ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        fun handleLayerView(
            focusLayoutManager: FocusLayoutManager?, view: View, viewLayer: Int,
            maxLayerCount: Int, position: Int, fraction: Float, offset: Float
        )

        /**
         * 处理正聚焦的那个View（即正处在从普通位置滚向聚焦位置时的那个view,即堆叠顶层view）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         * ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        fun handleFocusingView(
            focusLayoutManager: FocusLayoutManager?, view: View, position: Int,
            fraction: Float, offset: Float
        )

        /**
         * 处理不在堆叠里的普通view（正在聚焦的那个view除外）
         *
         * @param focusLayoutManager
         * @param view               view对象。请仅在方法体范围内对view做操作，不要外部强引用它，view是要被回收复用的
         * @param position           item所在的position
         * @param fraction           "一次完整的聚焦滑动"所在的进度百分比.百分比增加方向为向着堆叠移动的方向（即如果为FOCUS_LEFT
         * ，从右向左移动fraction将从0%到100%）
         * @param offset             当次滑动偏移量
         */
        fun handleNormalView(
            focusLayoutManager: FocusLayoutManager?, view: View, position: Int,
            fraction: Float, offset: Float
        )
    }

    /**
     * 简化版  滚动过程中view的变换监听接口。
     */
    abstract class SimpleTrasitionListener {
        /**
         * 返回堆叠view最大透明度
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        @FloatRange(from = 0.0, to = 1.0)
        fun getLayerViewMaxAlpha(maxLayerCount: Int): Float {
            return focusingViewMaxAlpha
        }

        /**
         * 返回堆叠view最小透明度
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        @FloatRange(from = 0.0, to = 1.0)
        fun getLayerViewMinAlpha(maxLayerCount: Int): Float {
            return 0f
        }

        /**
         * 返回堆叠view最大缩放比例
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        fun getLayerViewMaxScale(maxLayerCount: Int): Float {
            return focusingViewMaxScale
        }

        /**
         * 返回堆叠view最小缩放比例
         *
         * @param maxLayerCount 最大层级
         * @return
         */
        fun getLayerViewMinScale(maxLayerCount: Int): Float {
            return 0.7f
        }

        /**
         * 返回一个百分比值，相对于"一次完整的聚焦滑动"期间，在该百分比值内view就完成缩放、透明度的渐变变化。
         * 例：若返回值为1，说明在"一次完整的聚焦滑动"期间view将线性均匀完成缩放、透明度变化；
         * 例：若返回值为0.5，说明在"一次完整的聚焦滑动"的一半路程内（具体从什么时候开始变由实际逻辑自己决定），view将完成的缩放、透明度变化
         *
         * @return
         */
        @get:FloatRange(from = 0.0, to = 1.0)
        val layerChangeRangePercent: Float
            get() = 0.35f

        /**
         * 返回聚焦view的最大透明度
         *
         * @return
         */
        @get:FloatRange(from = 0.0, to = 1.0)
        val focusingViewMaxAlpha: Float
            get() = 1f

        /**
         * 返回聚焦view的最小透明度
         *
         * @return
         */
        @get:FloatRange(from = 0.0, to = 1.0)
        val focusingViewMinAlpha: Float
            get() = normalViewAlpha

        /**
         * 返回聚焦view的最大缩放比例
         *
         * @return
         */
        val focusingViewMaxScale: Float
            get() = 1.2f

        /**
         * 返回聚焦view的最小缩放比例
         *
         * @return
         */
        val focusingViewMinScale: Float
            get() = normalViewScale

        /**
         * 返回值意义参考[.getLayerChangeRangePercent]
         *
         * @return
         */
        @get:FloatRange(from = 0.0, to = 1.0)
        val focusingViewChangeRangePercent: Float
            get() = 0.5f

        /**
         * 返回普通view的透明度
         *
         * @return
         */
        @get:FloatRange(from = 0.0, to = 1.0)
        val normalViewAlpha: Float
            get() = 1.0f

        /**
         * 返回普通view的缩放比例
         *
         * @return
         */
        val normalViewScale: Float
            get() = 1.0f
    }

    /**
     * 将SimpleTrasitionListener转换成实际的TrasitionListener的转换器
     */
    class TrasitionListenerConvert(var stl: SimpleTrasitionListener) : TrasitionListener {
        override fun handleLayerView(
            focusLayoutManager: FocusLayoutManager?, view: View,
            viewLayer: Int, maxLayerCount: Int, position: Int,
            fraction: Float, offset: Float
        ) {
            /**
             * 期望效果：从0%开始到[SimpleTrasitionListener.getLayerChangeRangePercent] 期间
             * view均匀完成渐变，之后一直保持不变
             */
            //转换为真实的渐变变化百分比
            val realFraction: Float = if (fraction <= stl.layerChangeRangePercent) {
                fraction / stl.layerChangeRangePercent
            } else {
                1.0f
            }
            val minScale = stl.getLayerViewMinScale(maxLayerCount)
            val maxScale = stl.getLayerViewMaxScale(maxLayerCount)
            val scaleDelta = maxScale - minScale //总缩放差
            val currentLayerMaxScale =
                minScale + scaleDelta * (viewLayer + 1) / (maxLayerCount * 1.0f)
            val currentLayerMinScale = minScale + scaleDelta * viewLayer / (maxLayerCount * 1.0f)
            val realScale =
                currentLayerMaxScale - (currentLayerMaxScale - currentLayerMinScale) * realFraction
            val minAlpha = stl.getLayerViewMinAlpha(maxLayerCount)
            val maxAlpha = stl.getLayerViewMaxAlpha(maxLayerCount)
            val alphaDelta = maxAlpha - minAlpha //总透明度差
            val currentLayerMaxAlpha =
                minAlpha + alphaDelta * (viewLayer + 1) / (maxLayerCount * 1.0f)
            val currentLayerMinAlpha = minAlpha + alphaDelta * viewLayer / (maxLayerCount * 1.0f)
            val realAlpha =
                currentLayerMaxAlpha - (currentLayerMaxAlpha - currentLayerMinAlpha) * realFraction

            view.scaleX = realScale
            view.scaleY = realScale
            view.alpha = realAlpha
        }

        override fun handleFocusingView(
            focusLayoutManager: FocusLayoutManager?, view: View,
            position: Int, fraction: Float, offset: Float
        ) {
            /**
             * 期望效果：从0%开始到[SimpleTrasitionListener.getFocusingViewChangeRangePercent] 期间
             * view均匀完成渐变，之后一直保持不变
             */
            //转换为真实的渐变百分比
            val realFraction: Float = if (fraction <= stl.focusingViewChangeRangePercent) {
                fraction / stl.focusingViewChangeRangePercent
            } else {
                1.0f
            }
            val realScale =
                stl.focusingViewMinScale + (stl.focusingViewMaxScale - stl.focusingViewMinScale) * realFraction
            val realAlpha =
                stl.focusingViewMinAlpha + (stl.focusingViewMaxAlpha - stl.focusingViewMinAlpha) * realFraction
            view.scaleX = realScale
            view.scaleY = realScale
            view.alpha = realAlpha
        }

        override fun handleNormalView(
            focusLayoutManager: FocusLayoutManager?, view: View,
            position: Int, fraction: Float, offset: Float
        ) {
            /**
             * 期望效果：直接完成变换
             */
            view.scaleX = stl.normalViewScale
            view.scaleY = stl.normalViewScale
            view.alpha = stl.normalViewAlpha
        }
    }

    interface OnFocusChangeListener {
        /**
         * @param focusdPosition
         * @param lastFocusedPosition 可能为-1
         */
        fun onFocusChanged(focusdPosition: Int, lastFocusedPosition: Int)
    }

    companion object {
        const val TAG = "FocusLayoutManager"

        /**
         * 堆叠方向在左
         */
        const val FOCUS_LEFT = 1

        /**
         * 堆叠方向在右
         */
        const val FOCUS_RIGHT = 2

        /**
         * 堆叠方向在上
         */
        const val FOCUS_TOP = 3

        /**
         * 堆叠方向在下
         */
        const val FOCUS_BOTTOM = 4
        fun dp2px(context: Context, dp: Float): Float {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                context.resources.displayMetrics
            )
        }

        fun log(msg: String?) {
            log(TAG, msg)
        }

        fun log(tag: String?, msg: String?) {
            val isDebug = true
            if (isDebug) {
                Log.d(tag, msg!!)
            }
        }
    }
}