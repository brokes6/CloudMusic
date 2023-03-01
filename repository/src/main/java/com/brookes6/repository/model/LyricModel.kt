package com.brookes6.repository.model

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description:
 */
@TypeConverters(
    LyricOriginConverter::class
)
@kotlinx.serialization.Serializable
data class LyricModel(
    val sgc: Boolean = false,
    val sfy: Boolean = false,
    val qfy: Boolean = false,
    val lrc: LyricOrigin? = null,
    val tlyric: LyricOrigin? = null
) {
    @kotlinx.serialization.Serializable
    data class LyricOrigin(
        val version: Int = 0,
        val lyric: String = "",
    )
}

class LyricOriginConverter {
    @TypeConverter
    fun jsonToModel(json: String): LyricModel.LyricOrigin {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LyricModel.LyricOrigin): String {
        return Json.encodeToString(data)
    }
}
