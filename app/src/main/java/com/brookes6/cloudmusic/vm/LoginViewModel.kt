package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.toast
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.state.LoginState
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.brookes6.repository.model.QRImageModel
import com.brookes6.repository.model.QRKeyModel
import com.brookes6.repository.model.VerifyQRCodeModel
import com.drake.net.Post
import com.drake.serialize.serialize.serialize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Response

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class LoginViewModel : ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private var mQRCodeKey: String = ""

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: LoginAction) {
        when (action) {
            is LoginAction.PhoneLogin -> login(
                action.phone,
                action.password,
                action.captcha,
                action.onNavController
            )
            is LoginAction.SendPhoneCode -> sendPhoneCode(action.phone)
            is LoginAction.CreateQRCode -> createQRCode(action.onQRCodeCallback)
            is LoginAction.JudgeQRCodeState -> judgeQRCode(action.onNavController)
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
        captcha: String = "",
        onNavController: (String) -> Unit = {}
    ) {
        scopeNetLife {
            Post<LoginModel>(Api.PHONE_LOGIN) {
                param("phone", phone)
                if (password.isNotEmpty()) {
                    param("password", password)
                }
                if (captcha.isNotEmpty()) {
                    param("captcha", captcha)
                }
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

    private fun sendPhoneCode(phone: String) {
        scopeNetLife {
            Post<Response>(Api.SEND_PHONE_CODE) {
                param("phone", phone)
            }.await().also {
                if (it.code == 200) {
                    toast("验证码已发送")
                    state.isShowPhoneCode.value = true
                }
            }
        }
    }

    /**
     * 创建登录二维码
     *
     * @param onQRCodeCallback
     * @receiver
     */
    private fun createQRCode(onQRCodeCallback: (imgSrc: String) -> Unit = {}) {
        scopeNetLife {
            Post<QRKeyModel>(Api.GENERATE_RQCODE_KEY).await().also {
                Post<QRImageModel>(Api.CREATE_RQCODE) {
                    if (it.code != 200) {
                        toast("二维码生成失败，请重试~")
                        return@Post
                    }
                    mQRCodeKey = it.unikey
                    param("key", it.unikey)
                    param("qrimg", 1)
                }.await().also {
                    onQRCodeCallback.invoke(it.qrimg)
                }
            }
        }
    }

    private fun judgeQRCode(
        onNavController: (String) -> Unit = {}
    ) {
        scopeNetLife {
            Post<VerifyQRCodeModel>(Api.VERIFY_QRCODE) {
                param("key", mQRCodeKey)
            }.await().also {
                if (it.code == 803) {
                    state.mQRCodeStatus.value = true
                    serialize("cookie" to it.cookie)
                    onNavController(RouteConstant.HOME_PAGE)
                }
            }
        }
    }

    sealed class LoginAction {
        class PhoneLogin(
            val phone: String,
            val password: String,
            val onNavController: (String) -> Unit = {},
            val captcha: String = ""
        ) : LoginAction()

        class SendPhoneCode(
            val phone: String
        ) : LoginAction()

        class CreateQRCode(val onQRCodeCallback: (imgSrc: String) -> Unit = {}) : LoginAction()

        class JudgeQRCodeState(val onNavController: (String) -> Unit = {}) : LoginAction()
    }
}