package com.brookes6.cloudmusic.vm

import android.util.Base64
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.constant.AppConstant.IS_LOGIN
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.toast
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.state.LoginState
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.CookieModel
import com.brookes6.repository.model.QRImageModel
import com.brookes6.repository.model.QRKeyModel
import com.brookes6.repository.model.UserModel
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
                action.token,
                action.onNavController
            )

            is LoginAction.SendPhoneCode -> sendPhoneCode(action.phone)
            is LoginAction.CreateQRCode -> createQRCode(action.onQRCodeCallback)
            is LoginAction.JudgeQRCodeState -> judgeQRCode(action.onNavController, action.token)
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
        token: MutableLiveData<CookieModel>,
        onNavController: (String) -> Unit = {}
    ) {
        scopeNetLife {
            Post<UserModel>(Api.PHONE_LOGIN) {
                param("phone", phone)
                param("password", password)
                if (captcha.isNotEmpty()) {
                    param("captcha", captcha)
                }
            }.await().also {
                if (it.code == 200) {
                    serialize(IS_LOGIN to true)
                    token.postValue(CookieModel(cookie = it.cookie))
                    state.code.value = it.code
                    state.isLogin.value = true
                    state.isError.value = false
                    onNavController(RouteConstant.HOME_PAGE)
                } else {
                    serialize(IS_LOGIN to false)
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
    private fun createQRCode(onQRCodeCallback: (imgSrc: ByteArray) -> Unit = {}) {
        scopeNetLife {
            Post<QRKeyModel>(Api.GENERATE_RQCODE_KEY) {
                param("timestamp", System.currentTimeMillis())
            }.await().also {
                Post<QRImageModel>(Api.CREATE_RQCODE) {
                    if (it.code != 200) {
                        toast("二维码生成失败，请重试~")
                        return@Post
                    }
                    mQRCodeKey = it.unikey
                    param("key", it.unikey)
                    param("qrimg", true)
                    param("timestamp", System.currentTimeMillis())
                }.await().also {
                    val decodedString: ByteArray =
                        Base64.decode(it.qrimg.split(",")[1], Base64.DEFAULT)
                    onQRCodeCallback.invoke(decodedString)
                }
            }
        }
    }

    private fun judgeQRCode(
        onNavController: (String) -> Unit = {},
        token: MutableLiveData<CookieModel>,
    ) {
        scopeNetLife {
            Post<VerifyQRCodeModel>(Api.VERIFY_QRCODE) {
                param("key", mQRCodeKey)
            }.await().also {
                if (it.code == 803) {
                    token.postValue(CookieModel(cookie = it.cookie))
                    state.mQRCodeStatus.value = true
                    LogUtils.i("保存的Token为:${it.cookie}", "Token")
                    onNavController(RouteConstant.HOME_PAGE)
                } else {
                    toast(it.message)
                }
            }
        }
    }

    /**
     * 获取账号信息
     *
     */
    private fun getAccountInfo() {
        scopeNetLife {
            Post<UserModel>(Api.ACCOUNT_INFO).await().also {
                serialize(IS_LOGIN to true)
                state.code.value = it.code
                state.isLogin.value = true
                state.isError.value = false
                viewModelScope.launch(Dispatchers.IO) {
                    DataBaseManager.db?.userDao?.install(it)
                }
            }
        }
    }

    sealed class LoginAction {
        class PhoneLogin(
            val phone: String,
            val password: String,
            val onNavController: (String) -> Unit = {},
            val token: MutableLiveData<CookieModel>,
            val captcha: String = ""
        ) : LoginAction()

        class SendPhoneCode(
            val phone: String
        ) : LoginAction()

        class CreateQRCode(val onQRCodeCallback: (imgSrc: ByteArray) -> Unit = {}) : LoginAction()

        class JudgeQRCodeState(
            val onNavController: (String) -> Unit = {},
            val token: MutableLiveData<CookieModel>
        ) : LoginAction()
    }
}