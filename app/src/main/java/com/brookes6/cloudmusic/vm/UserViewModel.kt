package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.UserModel
import com.drake.net.Post
import com.drake.serialize.serialize.deserialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author: Sakura

 * Date: 2023/4/22

 * Description:
 */
class UserViewModel : ViewModel() {

    /**
     * 用户信息
     */
    private val _user: MutableLiveData<UserModel?> = MutableLiveData(null)
    val user: LiveData<UserModel?> = _user

    private var _userId: Long? = null

    fun dispatch(action: MyAction) {
        when (action) {
            is MyAction.GetUserInfo -> getUserInfo()
            else -> {}
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
                viewModelScope.launch(Dispatchers.IO) {
                    DataBaseManager.db?.userDao?.install(it)
                }
            }
        }
    }

    private fun getUserId(): Boolean {
        return when (_userId) {
            null -> {
                val userId: Long? = deserialize(AppConstant.USER_ID)
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