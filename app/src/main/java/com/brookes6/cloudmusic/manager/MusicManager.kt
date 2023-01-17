package com.brookes6.cloudmusic.manager

import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky

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

    fun play(songInfo: SongInfo) {
        StarrySky.with().let {
            if (it.isPaused()) {
                it.restoreMusic()
            } else {
                it.playMusicByInfo(songInfo)
            }
        }
    }

    fun pause() {
        StarrySky.with().pauseMusic()
    }

    fun stop() {
        StarrySky.with().stopMusic()
    }

    fun seekTo(time: Long) {
        StarrySky.with().seekTo(time)
    }

    fun next(notNextCallback: () -> Unit = {}) {
        StarrySky.with().let {
            if (it.isSkipToNextEnabled()) {
                StarrySky.with().skipToNext()
            } else {
                notNextCallback.invoke()
            }
        }
    }

    fun pre(notPreCallback: () -> Unit = {}) {
        StarrySky.with().let {
            if (it.isSkipToPreviousEnabled()) {
                StarrySky.with().skipToPrevious()
            } else {
                notPreCallback.invoke()
            }
        }
    }

    fun switchPlayModel(repeatMode: Int, isLoop: Boolean) {
        StarrySky.with().setRepeatMode(repeatMode, isLoop)
    }

    fun setPlayList(songList: MutableList<SongInfo>) {
        StarrySky.with().addPlayList(songList)
    }

    /**
     * 获取播放列表
     *
     * @return MutableList<SongInfo>
     */
    fun getPlayList(): MutableList<SongInfo> = StarrySky.with().getPlayList()

    fun clearPlayList() {
        StarrySky.with().clearPlayList()
    }

    fun setVolume() {

    }
}