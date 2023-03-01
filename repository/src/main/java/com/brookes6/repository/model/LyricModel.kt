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
    LyricOriginConverter::class,
    LyricCHTranslationConverter::class
)
@kotlinx.serialization.Serializable
data class LyricModel(
    val sgc: Boolean,
    val sfy: Boolean,
    val qfy: Boolean,
    val lrc: LyricOrigin?,
    val tlyric: LyricCHTranslation?
) {
    @kotlinx.serialization.Serializable
    data class LyricOrigin(
        val version: Int,
        val lyric: String,
    )

    @kotlinx.serialization.Serializable
    data class LyricCHTranslation(
        val version: Int,
        val lyric: String,
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

class LyricCHTranslationConverter {
    @TypeConverter
    fun jsonToModel(json: String): LyricModel.LyricCHTranslation {
        return Json.decodeFromString(json)
    }

    @TypeConverter
    fun modelToJson(data: LyricModel.LyricCHTranslation): String {
        return Json.encodeToString(data)
    }
}
