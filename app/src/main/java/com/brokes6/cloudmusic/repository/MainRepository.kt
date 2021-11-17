package com.brokes6.cloudmusic.repository

import com.brokes6.cloudmusic.api.ApiClient
import com.brokes6.cloudmusic.model.LoginModel

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

    suspend fun login(phone: String, password: String): LoginModel {
        return ApiClient.instance.api().login(phone, password)
    }

}