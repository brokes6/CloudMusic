package com.brookes6.cloudmusic.state

import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
data class MainState(
    val goPageType : MutableState<Int> = mutableStateOf(PAGE_TYPE.SPLASH),
    val isShowBottomTab: MutableState<Boolean> = mutableStateOf(false),
    val isShowSongController: MutableState<Boolean> = mutableStateOf(false),
    val currentPlayIndex : MutableState<Int> = mutableStateOf(0),
    val isLogin : MutableState<Boolean> = mutableStateOf(true)
)

@IntDef(PAGE_TYPE.SPLASH, PAGE_TYPE.LOGIN, PAGE_TYPE.HOME)
@Retention(AnnotationRetention.RUNTIME)
annotation class PAGE_TYPE {
    companion object {

        const val SPLASH = 0

        const val LOGIN = 1

        const val HOME = 2

    }
}
