package com.brokes6.cloudmusic.manager

import androidx.room.Room
import com.brokes6.cloudmusic.App
import com.brokes6.cloudmusic.db.AppDataBase

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description: Room 管理类
 */
class DataBaseManager private constructor() {

    private var db: AppDataBase? = null

    companion object {
        val instance: DataBaseManager by lazy { DataBaseManager() }
    }

    fun db(): AppDataBase? {
        return db ?: synchronized(this) {
            val dataBase = Room.databaseBuilder(
                App.mContext,
                AppDataBase::class.java,
                "CloudMusic"
            ).build()
            db = dataBase
            db
        }
    }

}