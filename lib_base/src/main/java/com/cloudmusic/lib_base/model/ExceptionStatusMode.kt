package com.cloudmusic.lib_base.model

import androidx.annotation.IntDef

/**

 * Author: fuxinbo



 * Date: 2022/7/28



 * Description:

 */
@IntDef(EXCEPTION_MODE.DATA_ERROR, EXCEPTION_MODE.DATA_EMPTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class EXCEPTION_MODE {
    companion object {

        /**

         * 请求出现错误

         */
        const val DATA_ERROR = 1

        /**

         * 请求结果为空

         */
        const val DATA_EMPTY = 2

    }
}