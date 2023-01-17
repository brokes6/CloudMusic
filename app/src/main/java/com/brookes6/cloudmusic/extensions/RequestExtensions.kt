package com.brookes6.cloudmusic.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.utils.LogUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.io.IOException

/**
 * Author: 付鑫博
 * Date: 2021/11/17
 * Description: 通过Kotlin协程与Flow流进行网络请求
 */


private const val TAG = "Request"

/**
 * 请求数据
 *
 * @param T 请求数据类型
 * @param operate 请求操作体
 * @param success 请求成功返回结果
 * @param error 请求错误返回结果
 * @param isShowLoadingDialog 是否开启loading效果(默认关闭)
 * @param isShowErrorView 是否开启错误页面(默认关闭)
 * @param delayTime loading效果结束延时(默认300毫秒)
 */
fun <T> ViewModel.request(
    operate: suspend CoroutineScope.() -> T,
    success: (T?) -> Unit = {},
    error: (String) -> Unit = {},
    isShowLoadingDialog: Boolean = true,
    isShowErrorView: Boolean = false,
    delayTime: Long = 0
) {
    viewModelScope.launch {
        flow {
            emit(operate())
        }.onStart {
//            if (isShowLoadingDialog) mUiChange.showLoadingDialog.postValue("")
        }.flowOn(Dispatchers.IO)
            .onEach {
                delay(delayTime)
            }
            .onCompletion {
//                if (isShowLoadingDialog) mUiChange.dismissLoadingDialog.postValue(true)
            }
            .catch {
                val exception = if (it is Exception) it else IOException()
                LogUtils.w("请求出现异常 --> ${exception.message}", TAG)
                error(exception.message ?: "网络出现异常")
            }
            .collect {
                success(it)
            }
    }
}

fun <T> GlobalScope.request(
    operate: suspend CoroutineScope.() -> T,
    success: (T?) -> Unit = {},
    error: (String) -> Unit = {},
    delayTime: Long = 0
) {
    launch {
        flow {
            emit(operate())
        }.flowOn(Dispatchers.IO)
            .onEach {
                delay(delayTime)
            }
            .catch {
                val exception = if (it is Exception) it else IOException()
                LogUtils.w("请求出现异常 --> ${exception.message}", TAG)
                error(exception.message ?: "网络出现异常")
            }
            .collect {
                success(it)
            }
    }
}