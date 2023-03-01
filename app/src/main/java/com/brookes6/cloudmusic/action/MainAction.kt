package com.brookes6.cloudmusic.action

import com.lzx.starrysky.SongInfo

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description: 与主页相关的行为
 */

sealed class MainAction {
    object GoHomePage : MainAction()

    object GoLoginPage : MainAction()

    class PlaySong(val index: Int, val list: MutableList<SongInfo>) : MainAction()

    object GetCurrentSong : MainAction()

    object ChangerSongDetailPage : MainAction()

    object NextSong : MainAction()

    object PreSong : MainAction()

    object PlayOrPauseSong : MainAction()

    /**
     * 获取当前音乐歌词
     *
     * @property id 音乐ID
     */
    object GetCurrentLyric : MainAction()
}