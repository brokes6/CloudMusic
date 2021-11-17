package com.brokes6.cloudmusic.repository

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class MainRepository private constructor() {

    companion object {
        val instance: MainRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { MainRepository() }
    }


}