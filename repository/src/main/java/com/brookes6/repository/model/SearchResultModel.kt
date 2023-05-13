package com.brookes6.repository.model

import kotlinx.serialization.Serializable

/**
 * Author: Sakura

 * Date: 2023/5/12

 * Description:
 */
@Serializable
data class SearchResultModel(
    val result: SearchResultInfo?,
    val code: Int? = 0
)

@Serializable
data class SearchResultInfo(
    val artists: List<SearchResultArtists?> = listOf(),
    val songs: List<SearchResultSong?> = listOf(),
    val albums: List<SearchResultAlbums?> = listOf(),
    val playlists: List<SearchResultPlaylists?> = listOf(),
    val userprofiles: List<SearchResultUser?> = listOf(),
    val mvs: List<SearchResultMV?> = listOf()
)

/**
 * 搜索结果作者
 */
@Serializable
data class SearchResultArtists(
    val id: Long? = 0,
    val name: String? = "",
    val picUrl: String? = "",
    val followed: Boolean? = false
)

/**
 * 搜索结果专辑
 */
@Serializable
data class SearchResultAlbums(
    val name: String? = "",
    val id: Long? = 0,
    val type: String? = "",
    val company: String? = "",
    val size: Int? = 0,
    val picUrl: String? = "",
    val artists: List<SearchResultSongArtists?> = listOf(),
)

/**
 * 搜索结果歌单
 */
@Serializable
data class SearchResultPlaylists(
    val id: Long? = 0,
    val name: String? = "",
    val coverImgUrl: String? = "",
    val playCount: Long? = 0,
    val description: String? = ""
)

/**
 * 搜索结果用户
 */
@Serializable
data class SearchResultUser(
    val nickname: String? = "",
    val userId: Long? = 0,
    val signature: String? = "",
    val avatarUrl: String? = "",
    val description: String? = ""
)

/**
 * 搜索结果歌曲
 */
@Serializable
data class SearchResultSong(
    val id: Long? = 0,
    val name: String? = "",
    val artists: List<SearchResultSongArtists?> = listOf(),
    val duration: Long? = 0,
    val fee: Int? = 0,
    val img1v1Url: String? = "",
    val al: SongImage?
)

/**
 * 搜索结果MV
 */
@Serializable
data class SearchResultMV(
    val id: Long? = 0,
    val cover: String? = "",
    val name: String? = "",
    val desc: String? = ""
)

@Serializable
data class SearchResultSongArtists(
    val id: Long? = 0,
    val name: String? = "",
    val picUrl: String? = "",
    val img1v1Url: String? = ""
)
