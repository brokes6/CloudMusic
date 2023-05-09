package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.brookes6.cloudmusic.action.SongPageAction
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.net.api.Api
import com.brookes6.repository.model.BoutiquePlayLiseInfo
import com.brookes6.repository.model.BoutiquePlayLiseModel
import com.drake.net.Get

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description:
 */
class SongPageViewModel : ViewModel() {

    private val _playList: MutableState<List<BoutiquePlayLiseInfo?>> = mutableStateOf(listOf())
    val playList: State<List<BoutiquePlayLiseInfo?>> = _playList

    fun dispatch(action: SongPageAction) {
        when (action) {
            is SongPageAction.GetSongPageData -> getSongPageData()
        }
    }

    /**
     * 获取精选歌单数据
     */
    private fun getSongPageData() {
        scopeDialog {
            if (_playList.value.isNotEmpty()) return@scopeDialog
            Get<BoutiquePlayLiseModel>(Api.GET_BOUTIQUE_PLAYLIST).await().also {
                _playList.value = it.playlists
            }
        }
    }
}