package com.brookes6.cloudmusic.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.brookes6.net.api.Api
import com.brookes6.repository.model.CookieModel
import com.drake.net.Get

/**
 * Author: Sakura

 * Date: 2023/4/22

 * Description:
 */
class TokenViewModel : ViewModel() {

    val token: MutableLiveData<CookieModel> = MutableLiveData()

    /**
     * 刷新token
     */
    fun refreshToken() {
        scopeNetLife {
            Get<CookieModel>(Api.REFRESH_COOKIE).await().also {
                token.value = it
            }
        }
    }
}