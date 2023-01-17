package com.brookes6.cloudmusic.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * @Author fuxinbo
 * @Date 2023/1/16 12:05
 * @Description TODO
 */
data class HomeState(
    val isLogin : MutableState<Boolean> = mutableStateOf(true)
)
