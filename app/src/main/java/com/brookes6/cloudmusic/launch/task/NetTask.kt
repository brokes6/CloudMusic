package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.IBaseTask
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.converter.SerializationConverter
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.serialize.serialize.deserialize

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class NetTask(val content: Application) : BaseTask() {

    override fun dependentTaskList(): MutableList<Class<out IBaseTask>> {
        return mutableListOf(MmkvTask::class.java)
    }

    override fun run() {
        // 当前使用的是本地服务
        NetConfig.initialize(Api.BASE_URL, content) {
            setDebug(BuildConfig.DEBUG)
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    val cookie: String? = deserialize("cookie")
                    LogUtils.i("当前使用的cookie --> $cookie}")
                    if (cookie?.isNotEmpty() == true) request.param("cookie", cookie, true)
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
}