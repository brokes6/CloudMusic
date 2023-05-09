package com.brookes6.cloudmusic.extensions

import com.brookes6.cloudmusic.App
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.utils.LogUtils
import com.drake.serialize.serialize.deserialize

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
fun checkCookie(code: Int, success: () -> Unit = {}) {
    if (code == 302) {
        LogUtils.w("Cookie失效，准备重新获取","Token")
        if (!deserialize(AppConstant.IS_LOGIN, false)) {
            LogUtils.w("当前用户未登录，不进行Cookie获取","Token")
            return
        }
        App.token.refreshToken {
            success.invoke()
        }
    }
}