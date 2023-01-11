package com.brookes6.cloudmusic.utils

import android.util.Log
import com.brookes6.cloudmusic.BuildConfig

/**
 * 日志输出工具类,只在debug模式下输出
 * Date: 2022/4/16
 *
 * @constructor Create empty Log utils
 */
object LogUtils {

    private const val TAG = "LogUtils"

    fun e(content: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, content)
        }
    }

    fun d(content: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, content)
        }
    }

    fun i(content: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, content)
        }
    }

    fun w(content: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.w(tag, content)
        }
    }

    fun v(content: String, tag: String = TAG) {
        if (BuildConfig.DEBUG) {
            Log.v(tag, content)
        }
    }

}