package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.action.HotSearchAction
import com.brookes6.cloudmusic.base.BaseViewModel
import com.brookes6.cloudmusic.bean.vo.SearchVO
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.SearchHistoryModel
import com.brookes6.repository.model.SearchHotDetailModel
import com.brookes6.repository.model.SearchResultModel
import com.drake.net.Get
import com.drake.net.Post
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
class SearchViewModel : BaseViewModel<HotSearchAction>() {

    val searchVO = mutableStateOf(SearchVO())

    override fun dispatch(action: HotSearchAction) {
        when (action) {
            is HotSearchAction.Search -> search()
            is HotSearchAction.GetSearchHotDetail -> getSearchBasicData()
            is HotSearchAction.SearchHot -> searchHot(action.searchHotText)
        }
    }

    /**
     * 搜索
     */
    private fun search() {
        if (searchVO.value.mSearchText.value.isEmpty()) return
        installSearchText(searchVO.value.mSearchText.value)
        if (!judgeIsLoad()) return
        scopeDialog {
            Post<SearchResultModel>(Api.SEARCH) {
                param("keywords", searchVO.value.mSearchText.value)
                param("type", getTypeForIndex())
                param("timestamp", System.currentTimeMillis())
            }.await().also {
                LogUtils.e(
                    "当前展示的页面为:${searchVO.value.mSelectSearchType}",
                    "TEST"
                )
                when (searchVO.value.mSelectSearchType) {
                    0 -> {
                        searchVO.value.mSearchResultSongData.value = it.result?.songs ?: listOf()
                    }

                    1 -> {
                        LogUtils.e(
                            "专辑个数 -> ${it.result?.albums?.size}",
                            "TEST"
                        )
                        searchVO.value.mSearchResultAlbumsData.value = it.result?.albums ?: listOf()
                    }

                    2 -> {
                        searchVO.value.mSearchResultArtistsData.value =
                            it.result?.artists ?: listOf()
                    }

                    3 -> {
                        searchVO.value.mSearchResultPlaylistsData.value =
                            it.result?.playlists ?: listOf()
                    }

                    4 -> {
                        searchVO.value.mSearchResultUserData.value =
                            it.result?.userprofiles ?: listOf()
                    }

                    5 -> {
                        searchVO.value.mSearchResultMVData.value = it.result?.mvs ?: listOf()
                    }
                }
            }
        }
    }

    private fun getSearchBasicData() {
        getSearchTextList()
        getSearchHotDetailData()
    }

    private fun searchHot(searchHotText: String?) {
        searchHotText ?: return
        searchVO.value.mSearchText.value = searchHotText
        installSearchText(searchHotText)
    }

    /**
     * 根据当前页面索引来获取对应Type
     */
    private fun getTypeForIndex(): Int {
        return when (searchVO.value.mSelectSearchType) {
            0 -> 1
            1 -> 10
            2 -> 100
            3 -> 1000
            4 -> 1002
            5 -> 1004
            else -> {
                1
            }
        }
    }

    private fun judgeIsLoad(): Boolean {
        return when (searchVO.value.mSelectSearchType) {
            0 -> {
                searchVO.value.mSearchResultSongData.value.isEmpty()
            }

            1 -> {
                searchVO.value.mSearchResultAlbumsData.value.isEmpty()
            }

            2 -> {
                searchVO.value.mSearchResultArtistsData.value.isEmpty()
            }

            3 -> {
                searchVO.value.mSearchResultPlaylistsData.value.isEmpty()
            }

            4 -> {
                searchVO.value.mSearchResultUserData.value.isEmpty()
            }

            5 -> {
                searchVO.value.mSearchResultMVData.value.isEmpty()
            }

            else -> {
                false
            }
        }
    }

    /**
     * 保存搜索历史
     */
    private fun installSearchText(searchText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            DataBaseManager.db?.searchHistoryDao?.install(SearchHistoryModel(searchText = searchText))
        }
    }

    /**
     * 获取搜索历史
     */
    private fun getSearchTextList() {
        viewModelScope.launch(Dispatchers.IO) {
            DataBaseManager.db?.searchHistoryDao?.getSearchHistoryList()?.let {
                searchVO.value.mSearchTextHistory.value = it
            }
        }
    }

    /**
     * 获取热搜列表(详细)
     */
    private fun getSearchHotDetailData() {
        scopeDialog(dispatcher = Dispatchers.Default) {
            Get<SearchHotDetailModel>(Api.GET_SEARCH_HOT_DETAIL).await().also {
                searchVO.value.mSearchHotDetailData.value = it.data
            }
        }
    }
}