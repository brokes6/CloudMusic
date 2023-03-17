package com.brookes6.cloudmusic.manager

import com.brookes6.cloudmusic.extensions.toast
import com.brookes6.cloudmusic.utils.LogUtils
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description: 音乐管理器
 */
class MusicManager private constructor() {

    companion object {
        val instance: MusicManager by lazy { MusicManager() }
    }

    fun play(songInfo: MutableList<SongInfo>, index: Int) {
        StarrySky.with()?.let {
            if (it.isPaused()) {
                it.restoreMusic()
            } else {
                it.playMusic(songInfo, index)
            }
        }
    }

    /**
     * 根据索引来进行播放对对应音乐，但必须先要设置好播放列表
     *
     * @param index
     */
    fun playOnly(index: Int) {
        StarrySky.with()?.playMusicByIndex(index)
    }

    fun playMusicForInfo(info: SongInfo) {
        StarrySky.with()?.prepareByInfo(info)
        StarrySky.with()?.playMusicByInfo(info)
    }

    fun playMusicByIndex(list: MutableList<SongInfo>, index: Int) {
        StarrySky.with()?.playMusic(list, index)
    }

    fun pause() {
        StarrySky.with()?.pauseMusic()
    }

    fun stop() {
        StarrySky.with()?.stopMusic()
    }

    fun seekTo(time: Long) {
        StarrySky.with()?.seekTo(time)
    }

    fun next(notNextCallback: () -> Unit = {}) {
        StarrySky.with()?.let {
            if (it.isSkipToNextEnabled()) {
                StarrySky.with()?.skipToNext()
            } else {
                notNextCallback.invoke()
            }
        }
    }

    fun pre(notPreCallback: () -> Unit = {}) {
        StarrySky.with()?.let {
            if (it.isSkipToPreviousEnabled()) {
                StarrySky.with()?.skipToPrevious()
            } else {
                notPreCallback.invoke()
            }
        }
    }

    fun switchPlayModel(repeatMode: Int, isLoop: Boolean) {
        StarrySky.with()?.setRepeatMode(repeatMode, isLoop)
    }

    fun setPlayList(songList: MutableList<SongInfo>) {
        StarrySky.with()?.let {
            it.clearPlayList()
            it.updatePlayList(songList)
        }
    }

    fun setPlayListAndPlay(index: Int, songList: MutableList<SongInfo>) {
        StarrySky.with()?.let {
            it.clearPlayList()
            it.updatePlayList(songList)
            it.playMusicByIndex(index)
        }
    }

    fun switchPlayModel() {
        StarrySky.with()?.let {
            when (it.getRepeatMode().repeatMode) {
                RepeatMode.REPEAT_MODE_NONE -> {
                    toast("单曲循环")
                    it.setRepeatMode(RepeatMode.REPEAT_MODE_ONE, true)
                }
                RepeatMode.REPEAT_MODE_ONE -> {
                    toast("随机播放")
                    it.setRepeatMode(RepeatMode.REPEAT_MODE_SHUFFLE, false)
                }
                RepeatMode.REPEAT_MODE_SHUFFLE -> {
                    toast("顺序播放")
                    it.setRepeatMode(RepeatMode.REPEAT_MODE_NONE, false)
                }
            }
        }
    }

    fun getPlayPosition(): Int {
        LogUtils.i("当前播放索引为 --> ${StarrySky.with()?.getNowIndex()}", "Song")
        return StarrySky.with()?.getNowIndex() ?: 0
    }

    fun getCurrentPlayModel(): Int =
        StarrySky.with()?.getRepeatMode()?.repeatMode ?: RepeatMode.REPEAT_MODE_NONE

    /**
     * 获取播放列表
     *
     * @return MutableList<SongInfo>
     */
    fun getPlayList(): MutableList<SongInfo>? = StarrySky.with()?.getPlayList()

    fun clearPlayList() {
        StarrySky.with()?.clearPlayList()
    }

    fun setVolume() {

    }
}