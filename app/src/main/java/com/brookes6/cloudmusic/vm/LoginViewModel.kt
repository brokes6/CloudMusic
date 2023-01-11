package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.brookes6.cloudmusic.launch.task.NetTask
import com.brookes6.cloudmusic.state.LoginState
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.drake.net.Get
import com.drake.serialize.serialize.serialize

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
            is LoginAction.PhoneLogin -> login(action.phone, action.password)
        }
    }

    /**
     * 手机号登陆
     *
     * @param phone 手机号
     * @param password 账号密码
     */
    private fun login(phone: Int, password: String) {
        scopeNetLife {
            Get<LoginModel>(Api.PHONE_LOGIN) {
                param("phone", "${phone}")
                param("password", password)
            }.await().also {
                if (it.code == 200) {
                    serialize("cookie" to it.cookie)
                    state.isLogin.value = true
                }
            }
        }
    }

    sealed class LoginAction {
        class PhoneLogin(val phone: Int, val password: String) : LoginAction()
    }

}