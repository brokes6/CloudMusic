package com.brookes6.cloudmusic.extensions

import android.widget.Toast
import com.brookes6.cloudmusic.App

/**
 * Author: fuxinbo

 * Date: 2023/2/19

 * Description:
 */

/**
 * 全局吐司
 *
 * @param message
 */
fun toast(message: String) {
    Toast.makeText(App.content, message, Toast.LENGTH_SHORT).show()
}