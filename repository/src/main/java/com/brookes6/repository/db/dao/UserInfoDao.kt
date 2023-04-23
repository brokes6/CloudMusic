package com.brookes6.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brookes6.repository.model.UserModel

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun install(data: UserModel)

    @Query("SELECT * FROM loginmodel")
    fun getUserInfo(): UserModel

}