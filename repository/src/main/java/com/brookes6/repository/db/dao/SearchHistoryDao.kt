package com.brookes6.repository.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.brookes6.repository.model.SearchHistoryModel

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
@Dao
interface SearchHistoryDao {

    /**
     * 保存当前搜索历史
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun install(data: SearchHistoryModel)

    /**
     * 查询搜索历史
     */
    @Query("SELECT * FROM searchhistorymodel")
    fun getSearchHistoryList(): List<SearchHistoryModel?>
}