package com.brokes6.cloudmusic.utils

import com.tencent.mmkv.MMKV

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/19
 * Mender:
 * Modify:
 * Description: 登陆页面SP工具类
 */
object SpUserInfoUtils {

    private val answerKv = MMKV.mmkvWithID("login_mmkv")

    private const val IS_LOGIN = "is_logon" // 是否登陆过了

    /**
     * 保存登陆状态
     *
     * @param isLogin
     */
    fun saveLoginState(isLogin: Boolean) {
        answerKv.encode(IS_LOGIN, isLogin)
    }

    /**
     * 是否登陆
     *
     * @return
     */
    fun isLogin(): Boolean = answerKv.decodeBool(IS_LOGIN, false)

}