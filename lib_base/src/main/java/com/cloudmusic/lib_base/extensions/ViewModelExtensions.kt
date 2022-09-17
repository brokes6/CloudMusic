package com.cloudmusic.lib_base.extensions

import java.lang.reflect.ParameterizedType

/**
 * Author: 付鑫博
 * Date: 2021/8/10
 * Description:
 */

@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}