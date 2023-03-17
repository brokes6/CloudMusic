package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description: 歌单详情数据模型
 */
@kotlinx.serialization.Serializable
data class PlayListDetailModel(
    val code: Int = 0,
    val songs: MutableList<PlayListDetailSongInfo> = mutableListOf(),
    val msg: String = "",
)

@kotlinx.serialization.Serializable
data class PlayListDetailSongInfo(
    val id: Long = 0L,
    val name: String = "",
    val ar: List<PlayListSongAuthorInfo?>,
    var al: PlayListSongCoverInfo?,
    val mark: Long = 0,
    val version: Int = 0,
    val copyright: Int = 0,
    val mv: Int = 0,
    val dt: Long = 0,
)

@kotlinx.serialization.Serializable
data class PlayListSongAuthorInfo(
    val id: Long = 0L,
    val name: String = ""
)

@kotlinx.serialization.Serializable
data class PlayListSongCoverInfo(
    val id: Long = 0L,
    val name: String = "",
    val picUrl: String = ""
)