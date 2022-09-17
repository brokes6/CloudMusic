package com.cloudmusic.home.manager

import android.app.Application
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

    private var isInit: Boolean = false

    fun init(application: Application, setDeBug: Boolean = true) {
        if (!isInit) {
            return
        }
        StarrySky.init(application).setDebug(setDeBug).apply()
        isInit = true
    }

    fun play() {

    }

    fun pause() {

    }

    fun stop() {

    }

    fun switchPlayModel() {

    }

    fun setPlayList() {

    }

    fun clearPlayList() {

    }

    fun setVolume() {

    }
}