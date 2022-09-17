package com.cloudmusic.lib_base.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cloudmusic.lib_base.throwable.ResponseThrowable
import com.cloudmusic.lib_repository.repository.BaseRepository

/**
 * Author: 付鑫博
 * Date: 2021/8/10
 * Description:
 */
open class BaseViewModel : ViewModel() {

    /**
     * 请求仓库
     */
    protected open val mRepository: BaseRepository = BaseRepository()

    /**
     * 定义公共UI
     */
    val mUiChange: UIChange by lazy { UIChange() }

    lateinit var responseThrowable: ResponseThrowable

    inner class UIChange {

        /**
         * 显示Loading
         */
        val showLoadingDialog by lazy { MutableLiveData<String>() }

        /**
         * 取消Loading
         */
        val dismissLoadingDialog by lazy { MutableLiveData<Boolean>() }

        /**
         *  显示Toast
         */
        val showToastView by lazy { MutableLiveData<String>() }

        /**
         * 显示失败View
         */
        val showErrorView by lazy { MutableLiveData<ResponseThrowable>() }

        /**
         * 显示空数据View
         */
        val showEmptyView by lazy { MutableLiveData<String>() }
    }
}