package com.brookes6.repository.model

/**
 * Author: Sakura

 * Date: 2023/3/18

 * Description:
 */
@kotlinx.serialization.Serializable
data class RecordMusicModel(
    val code: Int = 0,
    val data: RecordMusicInfo?,
    val message: String = ""
)

@kotlinx.serialization.Serializable
data class RecordMusicInfo(
    val total: Int = 0,
    val list: List<RecordMusicList?> = listOf(),
)

@kotlinx.serialization.Serializable
data class RecordMusicList(
    val resourceId: Long = 0,
    val playTime: Long = 0,
    val banned: Boolean = false,
    val data: DailySongs?,
)