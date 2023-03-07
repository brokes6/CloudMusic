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
import com.brookes6.repository.model.PlayListModel
import com.brookes6.repository.model.UserModel
import com.drake.net.Post
import com.drake.serialize.serialize.deserialize

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */
class MyViewModel : ViewModel() {

    /**
     * 用户信息
     */
    private val _user: MutableState<UserModel?> = mutableStateOf(null)
    val user: State<UserModel?> = _user

    /**
     * 用户歌单
     */
    private val _playList: MutableState<PlayListModel?> = mutableStateOf(null)
    val playList: State<PlayListModel?> = _playList

    private var _userId: Long? = null

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: MyAction) {
        when (action) {
            is MyAction.GetUserInfo -> getUserInfo()
            is MyAction.GetUserPlayList -> getUserPlayList()
        }
    }

    /**
     * 获取用户信息(内部自动获取当前用户id来进行请求)
     *
     */
    private fun getUserInfo() {
        if (_user.value != null) return
        if (!getUserId()) return
        scopeDialog {
            Post<UserModel>(Api.GET_USER_INFO) {
                param("uid", _userId)
            }.await().also {
                _user.value = it
            }
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
                _playList.value = it
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