package com.brookes6.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
@Entity
data class SearchHistoryModel(
    @PrimaryKey(autoGenerate = true)
    var searchId : Int = 0,
    var searchText : String = ""
)
