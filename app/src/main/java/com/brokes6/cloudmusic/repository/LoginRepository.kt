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
class LoginRepository private constructor() {

    companion object {
        val instance: LoginRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LoginRepository() }
    }

    suspend fun login(phone: String, password: String): LoginModel {
        return ApiClient.instance.api().login(phone, password)
    }

}