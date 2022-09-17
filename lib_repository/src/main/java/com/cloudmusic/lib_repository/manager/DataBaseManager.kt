package com.cloudmusic.lib_repository.manager

import android.content.Context
import androidx.room.Room
import com.cloudmusic.lib_repository.db.AppDataBase

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/23
 * Mender:
 * Modify:
 * Description: Room 管理类
 */
class DataBaseManager private constructor() {

    companion object {
        val instance: DataBaseManager by lazy { DataBaseManager() }

        var db: AppDataBase? = null
    }

    fun init(context: Context): AppDataBase? {
        return db ?: synchronized(this) {
            val dataBase = Room.databaseBuilder(
                context,
                AppDataBase::class.java,
                "CloudMusic"
            ).build()
            db = dataBase
            db
        }
    }

}