package com.brookes6.cloudmusic.state

import androidx.annotation.IntDef
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf


/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
data class MainState(
    val goPageType: MutableState<Int> = mutableStateOf(PAGE_TYPE.SPLASH),
    /**
     * 是否展示底部导航栏
     */
    val isShowBottomTab: MutableState<Boolean> = mutableStateOf(false),
    /**
     * 是否展示音乐控制器
     */
    val isShowSongController: MutableState<Boolean> = mutableStateOf(false),
    /**
     * 当前播放索引
     */
    val currentPlayIndex: MutableState<Int> = mutableStateOf(0),
    /**
     * 是否登录
     */
    val isLogin: MutableState<Boolean> = mutableStateOf(true),
    /**
     * 当前展示的页面
     */
    val currentRoute: MutableState<String> = mutableStateOf(""),

    /**
     * 是否展示音乐详情页
     */
    val isShowSongDetailPage: MutableState<Boolean> = mutableStateOf(false),

    val isInitPage: MutableState<Boolean> = mutableStateOf(true),
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
