package com.cloudmusic.lib_repository.db.dao

import androidx.room.*
import com.cloudmusic.lib_repository.bean.UserInfo

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description: 用户信息Dao类
 */
@Dao
interface UserDao {

    /**
     * 插入用户信息
     *
     * @param data UserInfo
     */
    @Insert
    suspend fun insertAll(data: UserInfo)

    /**
     * 删除全部用户信息
     *
     */
    @Delete
    suspend fun removeAll(data: UserInfo)

    /**
     * 更改用户信息
     *
     * @param data UserInfo
     */
    @Update
    suspend fun upData(data: UserInfo)

    /**
     * 获取全部保存的用户信息
     *
     * @return
     */
    @Query("SELECT * FROM userinfo")
    suspend fun getAllUserInfo(): MutableList<UserInfo>

    /**
     * 获取指定的用户信息
     *
     * @param uid
     * @return
     */
    @Query("SELECT * FROM userinfo WHERE userId = :uid")
    suspend fun getUserInfo(uid: Long): UserInfo
}