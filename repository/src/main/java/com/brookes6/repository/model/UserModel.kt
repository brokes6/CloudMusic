package com.brookes6.repository.model

import androidx.room.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description: 登陆返回数据实体类
 */
@Entity
@TypeConverters(AccountInfoConverter::class, UserInfoConverter::class)
@kotlinx.serialization.Serializable
data class UserModel(
    @PrimaryKey
    val id: Int = 1001,
    val loginType: Int = 0,
    val code: Int = 0,
    val message: String = "",
    val account: AccountInfo? = null,
    val token: String = "",
    val profile: UserInfo? = null,
    val cookie: String = ""
) {
    @Ignore
    constructor() : this(1001)

    @kotlinx.serialization.Serializable
    data class AccountInfo(
        val id: Long,
        val userName: String = "",
        val createTime: Long,
        val status : Int = 0,
        val vipType : Int = 0,
        val tokenVersion : Int = 0,
    )

    @kotlinx.serialization.Serializable
    data class UserInfo(
        val followed: Boolean,
        val backgroundUrl: String = "",
        val vipType: Int = 0,
        val userType: Int = 0,
        val nickname: String = "",
        val birthday: Long = 0,
        val avatarUrl: String = "",
        val gender: Int = 0,
        val createTime: Long = 0L,
        val follows : Int = 0,
        val followeds : Int = 0,
        val newFollows: Int = 0
    )
}

class AccountInfoConverter {
    @TypeConverter
    fun jsonToModel(json: String): UserModel.AccountInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: UserModel.AccountInfo): String {
        return Json.encodeToString(data)
    }
}

class UserInfoConverter {
    @TypeConverter
    fun jsonToModel(json: String): UserModel.UserInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: UserModel.UserInfo): String {
        return Json.encodeToString(data)
    }
}
