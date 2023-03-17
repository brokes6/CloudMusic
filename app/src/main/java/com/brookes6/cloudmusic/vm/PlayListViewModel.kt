package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.action.PlayListAction
import com.brookes6.cloudmusic.extensions.checkCookie
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.ui.widget.SmartSwipeRefreshState
import com.brookes6.cloudmusic.ui.widget.SmartSwipeStateFlag
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.PlayListDetailModel
import com.brookes6.repository.model.PlayListDetailSongInfo
import com.drake.net.Post
import com.lzx.starrysky.SongInfo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch


/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description:
 */
class PlayListViewModel : ViewModel() {

    private var mRequestIndex = 0

    private val _playList: SnapshotStateList<PlayListDetailSongInfo> =
        mutableStateListOf()
    val playList: SnapshotStateList<PlayListDetailSongInfo> = _playList

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: PlayListAction) {
        when (action) {
            is PlayListAction.GetPlayListDetail -> getPlayListDetail(
                action.requestIndex,
                action.id,
                action.state
            )
            is PlayListAction.PlaySong -> playSong(action.index)
        }
    }

    private fun getPlayListDetail(requestIndex: Int, id: Long, state: SmartSwipeRefreshState?) {
        if (id <= 0) return
        if (mRequestIndex == requestIndex && requestIndex != 0) return
        scopeDialog {
            try {
                Post<PlayListDetailModel>(Api.GET_PLAY_LIST_DETAIL) {
                    param("id", id)
                    param("limit", 50)
                    param("offset", 0 + (50 * requestIndex))
                }.await().also {
                    checkCookie(it.code)
                    state?.loadMoreFlag = SmartSwipeStateFlag.SUCCESS
                    mRequestIndex = requestIndex
                    if (_playList.isNotEmpty()) {
                        _playList.addAll(it.songs)
                        return@scopeDialog
                    }
                    _playList.clear()
                    _playList.addAll(it.songs)
                }
            } catch (e: Exception) {
                state?.loadMoreFlag = SmartSwipeStateFlag.ERROR
                e.printStackTrace()
            }
        }
    }

    private fun playSong(index: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            LogUtils.e("歌单音乐转换异常 --> ${throwable.message}", "Song")
        }) {
            val songList = mutableListOf<SongInfo>()
            _playList.forEachIndexed { index, it ->
                songList.add(
                    SongInfo(
                        songId = "${index + 1}",
                        songName = it.name,
                        artist = it.ar[0]?.name ?: "未知作者",
                        songCover = it.al?.picUrl ?: "",
                        index = index + 1,
                        id = it.id,
                        duration = it.dt
                    )
                )
            }.also {
                MusicManager.instance.setPlayListAndPlay(index, songList)
            }
        }
    }
}