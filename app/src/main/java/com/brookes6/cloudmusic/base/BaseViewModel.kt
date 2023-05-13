package com.brookes6.cloudmusic.base

import androidx.lifecycle.ViewModel

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
abstract class BaseViewModel<T> : ViewModel() {

    /**
     * 分发事件
     */
    abstract fun dispatch(action : T)

}