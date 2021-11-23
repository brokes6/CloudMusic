package com.brokes6.cloudmusic.utils

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.laboratory.baseclasslib.base.BaseViewModel
import com.laboratory.baseclasslib.throwable.ResponseThrowable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.IOException

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */

fun <T> BaseViewModel.request(
    operate: suspend CoroutineScope.() -> T,
    success: (T?) -> Unit = {},
    error: (ResponseThrowable) -> Unit = {},
    isShowLoadingDialog: Boolean = false,
) {
    viewModelScope.launch {
        flow {
            emit(operate())
        }.onStart {
            if (isShowLoadingDialog) mUiChange.showLoadingDialog.postValue("")
        }.flowOn(Dispatchers.IO)
            .onCompletion {
                if (isShowLoadingDialog) mUiChange.dismissLoadingDialog.postValue(true)
            }
            .catch {
                val exception = if (it is Exception) it else IOException()
                Log.e("Request", "出现异常 --> ${exception.message}")
                error(ResponseThrowable(-100, exception.message))
            }
            .collect {
                success(it)
            }
    }
}