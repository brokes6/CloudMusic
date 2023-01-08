package com.brookes6.cloudmusic.bean


/**
 * Author: 付鑫博
 * Date: 2021/9/27
 * Description:
 */
data class TabModel(
    val title: String,
    val iconNormal: Int,
    val iconChecked: Int,
    val isRepeatOnClick: Boolean = false
)
