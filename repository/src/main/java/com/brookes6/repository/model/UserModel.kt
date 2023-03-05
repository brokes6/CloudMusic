package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */
@kotlinx.serialization.Serializable
data class UserModel(
    val level: Int = 0,
    val listenSongs: Int = 0,
    val userPoint: UserPointInfo?,
    val mobileSign: Boolean = false,
    val pcSign: Boolean = false,
    val profile: ProfileInfo?,
    val code: Int = 0,
    val createDays: Int
)

@kotlinx.serialization.Serializable
data class UserPointInfo(
    val userId: Long = 0L,
    val balance: Int = 0,
    val version: Int = 0,
    val status: Int = 0,
    val updateTime: Long = 0
)

@kotlinx.serialization.Serializable
data class ProfileInfo(
    val nickname: String = "",
    val gender: Int = 0,
    val vipType: Int = 0,
    val userType: Int = 0,
    val createTime: Long = 0L,
    val avatarUrl: String = "",
    val follows : Int = 0,
    val followeds : Int = 0,
    val backgroundUrl: String = "",
    val newFollows: Int = 0
)