package com.brookes6.cloudmusic.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.CookieModel
import com.drake.net.Get
import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize

/**
 * Author: fuxinbo

 * Date: 2023/3/8

 * Description:
 */

/**
 * 检查当前请求是否返回302(cookie失效)
 *
 * @param code 响应码
 * @param success 重新获取Cookie回调
 * @receiver
 */
fun ViewModel.checkCookie(code: Int, success: () -> Unit = {}) {
    if (code == 302) {
        LogUtils.w("Cookie失效，准备重新获取")
        if (!deserialize(AppConstant.IS_LOGIN, false)) {
            LogUtils.w("当前用户未登录，不进行Cookie获取")
            return
        }
        scopeNetLife {
            Get<CookieModel>(Api.REFRESH_COOKIE).await().also {
                serialize(AppConstant.COOKIE to it.cookie)
                success.invoke()
            }
        }
    }
}