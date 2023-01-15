package com.brookes6.cloudmusic.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
data class MainState(
    val isShowHomePage: MutableState<Boolean> = mutableStateOf(false),
    val isShowBottomTab: MutableState<Boolean> = mutableStateOf(false),
    val isShowSongController: MutableState<Boolean> = mutableStateOf(false)
)