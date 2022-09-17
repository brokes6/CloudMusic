package com.cloudmusic.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.cloudmusic.lib_base.base.BaseViewModel
import com.cloudmusic.lib_base.extensions.request
import com.cloudmusic.lib_base.utils.SpUserInfoUtils
import com.cloudmusic.lib_repository.bean.UserInfo
import com.cloudmusic.lib_repository.repository.LoginRepository

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class LoginViewModel : BaseViewModel() {

    override val mRepository: LoginRepository by lazy(LazyThreadSafetyMode.NONE) { LoginRepository.instance }

    val mLoginInfo: MutableLiveData<UserInfo?> = MutableLiveData()

    val mError: MutableLiveData<String> = MutableLiveData()

    fun login(phone: String, password: String) {
        request({
            mRepository.login(phone, password)
        }, {
            mLoginInfo.postValue(it)
        }, {
            mError.postValue(it.message)
        })
    }

    /**
     * 保存用户数据
     *
     * @param data
     */
    fun saveUserInfo(data: UserInfo) {
        request({
            mRepository.saveUserInfo(data)
        })
    }

    /**
     * 是否登陆
     *
     * @return
     */
    fun isLogin(): Boolean {
        return SpUserInfoUtils.isLogin()
    }

    /**
     * 保存登陆状态
     *
     * @param isLogin
     */
    fun saveLoginState(isLogin: Boolean) {
        SpUserInfoUtils.saveLoginState(isLogin)
    }

}