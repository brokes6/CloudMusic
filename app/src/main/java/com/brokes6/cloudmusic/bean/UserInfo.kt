package com.brokes6.cloudmusic.bean

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description:
 */
@Entity
data class UserInfo(
    @PrimaryKey
    val userId: Long, // 用户ID
    val token: String?,
    val backgroundUrl: String?, // 背景图片
    val detailDescription: String?, // 介绍文案
    val nickname: String?, // 名字
    val birthday: Long, // 生日
    val avatarUrl: String?, // 头像
    val followeds: Int, // 粉丝数
    val follows: Int, // 关注数
    val playlistCount: Int, // 歌单数量
    val vipType: Int, // VIP等级
    val viptypeVersion: Long
)