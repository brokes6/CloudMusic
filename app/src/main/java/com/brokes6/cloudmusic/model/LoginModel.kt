package com.brokes6.cloudmusic.model

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
data class LoginModel(
    val loginType: Int,
    val code: Int,
    val account: AccountInfo,
    val token: String,
    val profile: Profile
) {
    data class AccountInfo(
        val id: Long,
        val userName: String,
        val type: Int,
        val status: Int,
        val createTime: Long,
        val vipType: Int,
        val viptypeVersion: Long
    )

    data class Profile(
        val backgroundUrl: String,
        val detailDescription: String,
        val userId: Long,
        val nickname: String, // 名字
        val birthday: Long, // 生日
        val avatarUrl: String, // 头像
        val province: String, // 省份
        val followeds: Int,
        val follows: Int,
        val playlistCount: Int
    )
}