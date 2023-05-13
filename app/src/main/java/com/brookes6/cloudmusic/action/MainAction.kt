package com.brookes6.cloudmusic.action

import com.brookes6.cloudmusic.bean.enum.BottomDialogEnum
import com.lzx.starrysky.SongInfo

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description: 与主页相关的行为
 */

sealed class MainAction {

    /**
     * 播放歌曲
     *
     * @property index 播放歌曲所在的索引未知
     * @property list 当前歌曲所在的列表
     */
    class PlaySong(val index: Int, val list: MutableList<SongInfo>) : MainAction()

    /**
     * 获取当前歌曲数据
     *
     */
    object GetCurrentSong : MainAction()

    /**
     * 展开/关闭音乐详情页
     *
     */
    object ChangerSongDetailPage : MainAction()

    /**
     * 下一首
     *
     */
    object NextSong : MainAction()

    /**
     * 上一首
     *
     */
    object PreSong : MainAction()

    /**
     * 切换播放模式
     *
     */
    object SwitchPlayModel : MainAction()

    /**
     * 暂停/播放音乐
     *
     */
    object PlayOrPauseSong : MainAction()

    /**
     * 获取当前音乐歌词
     *
     * @property id 音乐ID
     */
    object GetCurrentLyric : MainAction()

    /**
     * 展示音乐相关弹窗
     */
    class ShowMusicDialog(val type: BottomDialogEnum) : MainAction()
}