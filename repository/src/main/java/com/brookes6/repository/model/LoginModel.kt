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
data class LoginModel(
    @PrimaryKey
    val id: Int = 1001,
    val loginType: Int = -1,
    val code: Int = -1,
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
    )

    @kotlinx.serialization.Serializable
    data class UserInfo(
        val followed: Boolean,
        val backgroundUrl: String = "",
        val vipType: Int = -1,
        val userType: Int = -1,
        val nickname: String = "",
        val birthday: Long = 1,
        val avatarUrl: String = "",
    )
}

class AccountInfoConverter {
    @TypeConverter
    fun jsonToModel(json: String): LoginModel.AccountInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LoginModel.AccountInfo): String {
        return Json.encodeToString(data)
    }
}

class UserInfoConverter {
    @TypeConverter
    fun jsonToModel(json: String): LoginModel.UserInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LoginModel.UserInfo): String {
        return Json.encodeToString(data)
    }
}
