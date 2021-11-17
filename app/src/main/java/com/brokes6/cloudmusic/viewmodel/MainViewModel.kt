package com.brokes6.cloudmusic.viewmodel

import androidx.lifecycle.MutableLiveData
import com.brokes6.cloudmusic.model.LoginModel
import com.brokes6.cloudmusic.repository.MainRepository
import com.brokes6.cloudmusic.utils.request
import com.laboratory.baseclasslib.base.BaseViewModel

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class MainViewModel : BaseViewModel() {

    val mLoginInfo: MutableLiveData<LoginModel> = MutableLiveData()

    private val mRepository: MainRepository by lazy(LazyThreadSafetyMode.NONE) { MainRepository.instance }

    fun login(phone: String, password: String) {
        request({
            mRepository.login(phone, password)
        }, {
            mLoginInfo.postValue(it)
        }, {

        })
    }
}