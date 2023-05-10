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

    /**
     * 播放指定列表的指定音乐，若指定音乐处于暂停状态，则重新播放
     */
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

    /**
     * 播放指定音乐
     */
    fun playMusicForInfo(info: SongInfo) {
        StarrySky.with()?.prepareByInfo(info)
        StarrySky.with()?.playMusicByInfo(info)
    }

    /**
     * 播放指定列表的指定音乐
     */
    fun playMusicByIndex(list: MutableList<SongInfo>, index: Int) {
        StarrySky.with()?.playMusic(list, index)
    }

    /**
     * 暂停播放
     */
    fun pause() {
        StarrySky.with()?.pauseMusic()
    }

    /**
     * 停止播放
     */
    fun stop() {
        StarrySky.with()?.stopMusic()
    }

    /**
     * 跳转进度
     */
    fun seekTo(time: Long) {
        StarrySky.with()?.seekTo(time)
    }

    /**
     * 上一首
     */
    fun next(notNextCallback: () -> Unit = {}) {
        StarrySky.with()?.let {
            if (it.isSkipToNextEnabled()) {
                StarrySky.with()?.skipToNext()
            } else {
                notNextCallback.invoke()
            }
        }
    }

    /**
     * 下一首
     */
    fun pre(notPreCallback: () -> Unit = {}) {
        StarrySky.with()?.let {
            if (it.isSkipToPreviousEnabled()) {
                StarrySky.with()?.skipToPrevious()
            } else {
                notPreCallback.invoke()
            }
        }
    }

    /**
     * 设置指定的播放模式
     */
    fun switchPlayModel(repeatMode: Int, isLoop: Boolean) {
        StarrySky.with()?.setRepeatMode(repeatMode, isLoop)
    }

    /**
     * 设置播放列表
     */
    fun setPlayList(songList: MutableList<SongInfo>) {
        StarrySky.with()?.let {
            it.clearPlayList()
            it.updatePlayList(songList)
        }
    }

    /**
     * 设置播放列表并播放第一个音乐
     */
    fun setPlayListAndPlay(index: Int, songList: MutableList<SongInfo>) {
        StarrySky.with()?.let {
            it.clearPlayList()
            it.updatePlayList(songList)
            it.playMusicByIndex(index)
        }
    }

    /**
     * 切换播放模式
     *
     * 单曲循环 -> 随机播放 -> 顺序播放
     */
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

    /**
     * 获取当前播放索引
     */
    fun getPlayPosition(): Int {
        LogUtils.i("当前播放索引为 --> ${StarrySky.with()?.getNowIndex()}", "Song")
        return StarrySky.with()?.getNowIndex() ?: 0
    }

    /**
     * 获取当前播放模式
     */
    fun getCurrentPlayModel(): Int =
        StarrySky.with()?.getRepeatMode()?.repeatMode ?: RepeatMode.REPEAT_MODE_NONE

    /**
     * 获取播放列表
     *
     * @return MutableList<SongInfo>
     */
    fun getPlayList(): MutableList<SongInfo> = StarrySky.with()?.getPlayList() ?: mutableListOf()

    /**
     * 清除播放列表
     */
    fun clearPlayList() {
        StarrySky.with()?.clearPlayList()
    }

    /**
     * 设置播放声音大小
     */
    fun setVolume() {

    }
}