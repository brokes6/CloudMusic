package com.cloudmusic.lib_base.throwable

/**
 * Author: 付鑫博
 * Date: 2021/8/10
 * Description:
 */
data class ResponseThrowable(
    var errorCode: Int,
    var msg: String?
) : Throwable()