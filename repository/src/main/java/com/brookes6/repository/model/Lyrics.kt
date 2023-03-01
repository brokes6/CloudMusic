package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description:
 */
data class Lyrics(
    val time: Long,
    val lyric: String,
    val lyricSub : String? = "",
)
