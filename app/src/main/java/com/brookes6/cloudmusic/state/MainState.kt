package com.brookes6.cloudmusic.state

import androidx.annotation.IntDef
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.brookes6.cloudmusic.bean.enum.BottomDialogEnum


/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
data class MainState(

    /**
     * 是否展示底部导航栏
     */
    val isShowBottomTab: MutableState<Boolean> = mutableStateOf(false),

    /**
     * 需要展示的弹窗类型
     */
    val mShowDialogType: MutableState<Int> = mutableStateOf(BottomDialogEnum.NORMAL.ordinal),

    /**
     * 当前展示的tab索引
     */
    val currentBottomTabIndex: MutableState<Int> = mutableStateOf(0),

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

    /**
     * 是否为第一次初始化页面
     */
    val isInitPage: MutableState<Boolean> = mutableStateOf(true),

    val isInitPage2: MutableState<Boolean> = mutableStateOf(true),

    /**
     * 当前音乐播放进度
     */
    val mProgress: MutableState<Float> = mutableStateOf(0f),

    /**
     * 当前播放状态
     */
    val mPlayStatus: MutableState<Int> = mutableStateOf(PLAY_STATUS.NOMAL),

    /**
     * 是否展示歌词
     */
    val mIsShowLyric: MutableState<Boolean> = mutableStateOf(false),

    /**
     * 是否重置歌词
     */
    val mResetLyric: MutableState<Boolean> = mutableStateOf(false),

    /**
     * 当前歌词展示索引
     */
    val mCurrentLyricIndex: MutableState<Int> = mutableStateOf(0),

    /**
     * 当前播放时长
     */
    var mCurrentPlayTime: MutableState<Long> = mutableStateOf(0L)
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

@IntDef(PLAY_STATUS.NOMAL, PLAY_STATUS.PLAYING, PLAY_STATUS.PAUSE)
@Retention(AnnotationRetention.RUNTIME)
annotation class PLAY_STATUS {
    companion object {

        const val NOMAL = 1

        const val PLAYING = 2

        const val PAUSE = 3

    }
}
