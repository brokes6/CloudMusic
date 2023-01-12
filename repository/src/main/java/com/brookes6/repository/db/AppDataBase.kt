package com.brookes6.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brookes6.repository.db.dao.UserInfoDao
import com.brookes6.repository.model.LoginModel

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description:
 */
@Database(entities = [LoginModel::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract val userDao: UserInfoDao

}