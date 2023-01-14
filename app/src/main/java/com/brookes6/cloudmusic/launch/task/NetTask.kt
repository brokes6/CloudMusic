package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.IBaseTask
import com.brookes6.repository.converter.SerializationConverter
import com.drake.net.NetConfig
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.serialize.serialize.serialLazy

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class NetTask(val content : Application) : BaseTask() {

    private var cookie: String by serialLazy("",name = "cookie")

    override fun dependentTaskList(): MutableList<Class<out IBaseTask>> {
        return mutableListOf(MmkvTask::class.java)
    }

    override fun run() {
        // 当前使用的是本地服务
        NetConfig.initialize("http://192.168.31.237:3000", content) {
            setDebug(BuildConfig.DEBUG)
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    if (cookie.isNotEmpty()) request.param("cookie",cookie)
                }
            })
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