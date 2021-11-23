package com.brokes6.cloudmusic.viewmodel

import com.brokes6.cloudmusic.constants.MainConstants.CLICK_INTERVALS
import com.laboratory.baseclasslib.base.BaseViewModel

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class MainViewModel : BaseViewModel() {

    private var mLastClickTime: Long = 0


    /**
     * 检查是否满足退出条件
     *
     * @return
     */
    fun checkExit(): Boolean {
        val now = System.currentTimeMillis()
        return if (now - mLastClickTime > CLICK_INTERVALS) {
            mLastClickTime = now
            false
        } else {
            true
        }
    }
}