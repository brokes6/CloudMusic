package com.brokes6.cloudmusic.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brokes6.cloudmusic.bean.UserInfo
import com.brokes6.cloudmusic.db.dao.UserDao

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description:
 */
@Database(entities = [UserInfo::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract val userDao: UserDao

}