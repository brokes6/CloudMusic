package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.IBaseTask
import com.brookes6.cloudmusic.ui.dialog.LoadingDialog
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.converter.SerializationConverter
import com.brookes6.repository.model.CookieModel
import com.drake.net.Get
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.net.utils.scopeNet
import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class NetTask(val content: Application) : BaseTask() {

    private var mIsRefresh : Boolean = false

    override fun dependentTaskList(): MutableList<Class<out IBaseTask>> {
        return mutableListOf(MmkvTask::class.java)
    }

    override fun run() {
        // 当前使用的是本地服务
        NetConfig.initialize(Api.BASE_URL, content) {
            setDebug(BuildConfig.DEBUG)
            setDialogFactory { LoadingDialog(it) }
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    val cookie: String? = deserialize(AppConstant.COOKIE)
                    LogUtils.i("当前使用的cookie --> \n$cookie")
                    if (!cookie.isNullOrEmpty()) {
                        request.param("cookie", cookie, true)
                    } else {
                        refreshCookie()
                    }
                }
            })
            cookieJar(PersistentCookieJar(content))
            addInterceptor(LogRecordInterceptor(BuildConfig.DEBUG))
            setConverter(SerializationConverter())
        }
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return false
    }

    /**
     * 当cookie不存在时，自动获取Cookie
     *
     */
    private fun refreshCookie() {
        if (mIsRefresh) return
        if (!deserialize(AppConstant.IS_LOGIN, false)) {
            LogUtils.w("当前用户未登录，不进行Cookie获取")
            return
        }
        mIsRefresh = true
        LogUtils.i("本地Cookie为空，重新获取Cookie")
        scopeNet {
            Get<CookieModel>(Api.REFRESH_COOKIE).await().also {
                serialize(AppConstant.COOKIE to it.cookie)
                mIsRefresh = false
            }
        }
    }
}