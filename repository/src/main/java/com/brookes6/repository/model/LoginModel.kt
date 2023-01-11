package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description: 登陆返回数据实体类
 */
@kotlinx.serialization.Serializable
data class LoginModel(
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
