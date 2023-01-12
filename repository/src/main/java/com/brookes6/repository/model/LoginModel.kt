package com.brookes6.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description: 登陆返回数据实体类
 */
@Entity
@TypeConverters(AccountInfoConverter::class,UserInfoConverter::class)
@kotlinx.serialization.Serializable
data class LoginModel(
    @PrimaryKey
    val id : Int = 1001,
    val loginType : Int,
    val code : Int,
    val message : String,
    val account : AccountInfo,
    val token : String,
    val profile : UserInfo,
    val cookie : String
){
    @kotlinx.serialization.Serializable
    data class AccountInfo(
        val id : Int,
        val createTime : Int,
    )

    @kotlinx.serialization.Serializable
    data class UserInfo(
        val followed : Boolean,
        val backgroundUrl : String,
        val vipType : Int,
        val userType : Int,
        val nickname : String,
        val birthday : Int,
        val avatarUrl : String,
    )
}

class AccountInfoConverter{
    @TypeConverter
    fun jsonToModel(json: String): LoginModel.AccountInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LoginModel.AccountInfo): String {
        return Json.encodeToString(data)
    }
}

class UserInfoConverter{
    @TypeConverter
    fun jsonToModel(json: String): LoginModel.UserInfo {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LoginModel.UserInfo): String {
        return Json.encodeToString(data)
    }
}
