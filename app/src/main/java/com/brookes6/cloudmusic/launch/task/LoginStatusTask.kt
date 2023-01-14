package com.brookes6.cloudmusic.launch.task

import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.IBaseTask
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.drake.net.Get
import com.drake.net.utils.scopeNet
import com.drake.serialize.serialize.serialize

/**
 * @Author fuxinbo
 * @Date 2023/1/13 17:11
 * @Description TODO
 */
class LoginStatusTask : BaseTask() {

    override fun dependentTaskList(): MutableList<Class<out IBaseTask>> {
        return mutableListOf(NetTask::class.java)
    }

    override fun run() {
        scopeNet {
            Get<LoginModel>(Api.LOGIN_STATUS).await().also {
                LogUtils.d("账号状态为 -> $it")
                if (it.loginType == -1) {
                    serialize("isLogin" to false)
                } else {
                    serialize("isLogin" to true)
                }
            }
        }
    }

    override fun isRunImmediately(): Boolean {
        return false
    }

    override fun isOnMainThread(): Boolean {
        return true
    }


}