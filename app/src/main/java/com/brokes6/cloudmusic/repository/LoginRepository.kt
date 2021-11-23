package com.brokes6.cloudmusic.repository

import android.util.Log
import com.brokes6.cloudmusic.api.ApiClient
import com.brokes6.cloudmusic.bean.UserInfo
import com.brokes6.cloudmusic.manager.DataBaseManager

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description: login页面Repository
 */
class LoginRepository private constructor() {

    companion object {
        val instance: LoginRepository by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { LoginRepository() }

        val TAG = LoginRepository::class.java.simpleName
    }

    /**
     * 登陆
     *
     * @param phone 手机号
     * @param password 密码
     * @return
     */
    suspend fun login(phone: String, password: String): UserInfo? {
        val loginResponse = ApiClient.instance.api().login(phone, password)
        val userInfo: UserInfo
        loginResponse.profile?.run {
            userInfo = UserInfo(
                userId,
                loginResponse.token,
                backgroundUrl,
                detailDescription,
                nickname,
                birthday,
                avatarUrl,
                followeds,
                follows,
                playlistCount,
                loginResponse.account?.vipType ?: 0,
                loginResponse.account?.viptypeVersion ?: 0L
            )
            return userInfo
        }
        Log.e(TAG, "用户信息返回错误!")
        return null
    }

    /**
     * 保存用户信息
     *
     * @param data
     */
    suspend fun saveUserInfo(data: UserInfo) {
        DataBaseManager.instance.db()?.userDao?.insertAll(data)
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    suspend fun getUserInfo(): UserInfo? {
        return DataBaseManager.instance.db()?.userDao?.getAllUserInfo()?.get(0)
    }
}