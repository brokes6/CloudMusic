package com.brookes6.cloudmusic.launch.task

import android.app.Application
import com.brookes6.cloudmusic.BuildConfig
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.launch.BaseTask
import com.brookes6.cloudmusic.launch.IBaseTask
import com.brookes6.cloudmusic.ui.dialog.LoadingDialog
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.TokenViewModel
import com.brookes6.net.api.Api
import com.brookes6.repository.converter.SerializationConverter
import com.drake.net.NetConfig
import com.drake.net.cookie.PersistentCookieJar
import com.drake.net.interceptor.LogRecordInterceptor
import com.drake.net.interceptor.RequestInterceptor
import com.drake.net.okhttp.setConverter
import com.drake.net.okhttp.setDebug
import com.drake.net.okhttp.setDialogFactory
import com.drake.net.okhttp.setRequestInterceptor
import com.drake.net.request.BaseRequest
import com.drake.serialize.serialize.deserialize

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
class NetTask(val content: Application) : BaseTask() {

    private var mIsRefresh: Boolean = false

    private var mTokenVM: TokenViewModel? = null

    override fun dependentTaskList(): MutableList<Class<out IBaseTask>> {
        return mutableListOf(MmkvTask::class.java)
    }

    override fun run() {
        NetConfig.initialize(Api.BASE_URL, content) {
            setDebug(BuildConfig.DEBUG)
            setDialogFactory { LoadingDialog(it) }
            setRequestInterceptor(object : RequestInterceptor {
                override fun interceptor(request: BaseRequest) {
                    val cookie: String? = deserialize(AppConstant.COOKIE)
//                    val cookie: String? = "MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_R_T=; Max-Age=0; Expires=Thu, 16 Mar 2023 06:12:06 GMT; Path=/;;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/api/feedback; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/eapi/clientlog; HTTPOnly;__csrf=9222307561baa571ab4d6cc93b0a6f2f; Max-Age=1296010; Expires=Fri, 31 Mar 2023 06:12:16 GMT; Path=/;;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/api/feedback; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/neapi/clientlog; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/weapi/clientlog; HTTPOnly;MUSIC_SNS=; Max-Age=0; Expires=Thu, 16 Mar 2023 06:12:06 GMT; Path=/;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/neapi/feedback; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/api/clientlog; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/eapi/clientlog; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/eapi/feedback; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/wapi/feedback; HTTPOnly;MUSIC_U=975d071fd4550270fed4eade93d70d4d0d6cfd30f5ef14cb7aec2232642a5d6e8a08bd5bf851808f56a803b2a03ed0cad562aa5e86ce762f6e724be969838e397001b15ffad391f0a0d2166338885bd7; Max-Age=15552000; Expires=Tue, 12 Sep 2023 06:12:06 GMT; Path=/; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/weapi/feedback; HTTPOnly;MUSIC_A_T=1487474978312; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/openapi/clientlog; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/wapi/clientlog; HTTPOnly;MUSIC_R_T=1487474978318; Max-Age=2147483647; Expires=Tue, 03 Apr 2091 09:26:13 GMT; Path=/weapi/feedback; HTTPOnly"
                    if (!cookie.isNullOrEmpty()) {
                        request.param("cookie", cookie, false)
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
        mTokenVM?.refreshToken()
        mIsRefresh = false
    }

    fun setTokenVM(tokenVM: TokenViewModel) {
        mTokenVM = tokenVM
    }
}