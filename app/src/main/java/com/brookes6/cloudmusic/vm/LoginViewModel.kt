package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.state.LoginState
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.drake.net.Get
import com.drake.serialize.serialize.serialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: LoginAction) {
        when (action) {
            is LoginAction.PhoneLogin -> login(action.phone, action.password,action.onNavController)
        }
    }

    /**
     * 手机号登陆
     *
     * @param phone 手机号
     * @param password 账号密码
     */
    private fun login(
        phone: String,
        password: String,
        onNavController: (String) -> Unit = {}
    ) {
        scopeNetLife {
            Get<LoginModel>(Api.PHONE_LOGIN) {
                param("phone", phone)
                param("password", password)
            }.await().also {
                if (it.code == 200) {
                    serialize("isLogin" to true)
                    serialize("cookie" to it.cookie)
                    state.code.value = it.code
                    state.isLogin.value = true
                    state.isError.value = false
                    viewModelScope.launch(Dispatchers.IO) {
                        DataBaseManager.db?.userDao?.install(it)
                    }
                    onNavController(RouteConstant.HOME_PAGE)
                } else {
                    serialize("isLogin" to false)
                    state.code.value = it.code
                    state.isError.value = true
                    state.message.value = it.message ?: "发生错误"
                }
            }
        }
    }

    sealed class LoginAction {
        class PhoneLogin(
            val phone: String,
            val password: String,
            val onNavController: (String) -> Unit = {}
        ) : LoginAction()
    }

}