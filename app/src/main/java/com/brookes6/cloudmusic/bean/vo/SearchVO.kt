package com.brookes6.cloudmusic.bean.vo

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.brookes6.repository.model.SearchHistoryModel
import com.brookes6.repository.model.SearchHotDetailInfo
import com.brookes6.repository.model.SearchResultAlbums
import com.brookes6.repository.model.SearchResultArtists
import com.brookes6.repository.model.SearchResultMV
import com.brookes6.repository.model.SearchResultPlaylists
import com.brookes6.repository.model.SearchResultSong
import com.brookes6.repository.model.SearchResultUser

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
data class SearchVO(
    /**
     * 当前搜索关键字
     */
    val mSearchText: MutableState<String> = mutableStateOf(""),

    /**
     * 当前搜索历史
     */
    val mSearchTextHistory: MutableState<List<SearchHistoryModel?>> = mutableStateOf(listOf()),

    /**
     * 热搜列表(详细)
     */
    val mSearchHotDetailData: MutableState<List<SearchHotDetailInfo?>> = mutableStateOf(listOf()),

    var mSelectSearchType : Int = 0,

    /**
     * 歌曲搜索
     */
    val mSearchResultSongData: MutableState<List<SearchResultSong?>> = mutableStateOf(listOf()),

    /**
     * 作者搜索
     */
    val mSearchResultArtistsData: MutableState<List<SearchResultArtists?>> = mutableStateOf(listOf()),

    /**
     * 专辑搜索
     */
    val mSearchResultAlbumsData: MutableState<List<SearchResultAlbums?>> = mutableStateOf(listOf()),

    /**
     * 歌单搜索
     */
    val mSearchResultPlaylistsData: MutableState<List<SearchResultPlaylists?>> = mutableStateOf(
        listOf()
    ),

    /**
     * 用户搜索
     */
    val mSearchResultUserData: MutableState<List<SearchResultUser?>> = mutableStateOf(listOf()),

    /**
     * MV搜索
     */
    val mSearchResultMVData: MutableState<List<SearchResultMV?>> = mutableStateOf(listOf()),
)
