package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.constant.AppConstant.USER_ID
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.PlayListInfo
import com.brookes6.repository.model.PlayListModel
import com.drake.net.Post
import com.drake.serialize.serialize.deserialize

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */
class MyViewModel : ViewModel() {

    private val _userLikePlayList: MutableState<PlayListInfo?> = mutableStateOf(null)
    val userLikePlayList: State<PlayListInfo?> = _userLikePlayList

    /**
     * 原始数据
     */
    val playInfo: MutableState<PlayListInfo?> = mutableStateOf(null)

    /**
     * 用户歌单
     */
    private val _playList: MutableState<MutableList<PlayListInfo?>?> = mutableStateOf(null)
    val playList: State<MutableList<PlayListInfo?>?> = _playList

    private var _userId: Long? = null

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: MyAction) {
        when (action) {
            is MyAction.GetUserPlayList -> getUserPlayList()
            else -> {}
        }
    }

    /**
     * 获取用户歌单
     *
     */
    private fun getUserPlayList() {
        if (_playList.value != null) return
        if (!getUserId()) return
        scopeDialog {
            Post<PlayListModel>(Api.GET_USER_PLAYLIST) {
                param("uid", _userId)
            }.await().also {
                _userLikePlayList.value = it.playlist.getOrNull(0)
                _playList.value = it.playlist.toMutableList()
                _playList.value?.removeAt(0)
            }
        }
    }

    private fun getUserId(): Boolean {
        return when (_userId) {
            null -> {
                val userId: Long? = deserialize(USER_ID)
                return if (userId == null) {
                    LogUtils.w("用户ID为空！")
                    false
                } else {
                    _userId = userId
                    true
                }
            }

            else -> {
                true
            }
        }
    }
}