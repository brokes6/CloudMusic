package com.brookes6.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brookes6.repository.db.dao.SearchHistoryDao
import com.brookes6.repository.db.dao.UserInfoDao
import com.brookes6.repository.model.SearchHistoryModel
import com.brookes6.repository.model.UserModel

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description:
 */
@Database(entities = [UserModel::class, SearchHistoryModel::class], version = 1)
abstract class AppDataBase : RoomDatabase() {

    abstract val userDao: UserInfoDao

    abstract val searchHistoryDao : SearchHistoryDao

}