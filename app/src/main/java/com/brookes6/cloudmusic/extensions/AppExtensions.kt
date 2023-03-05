package com.brookes6.cloudmusic.extensions

import android.app.Dialog
import android.widget.Toast
import com.brookes6.cloudmusic.App
import com.brookes6.cloudmusic.MainActivity
import com.drake.net.scope.DialogCoroutineScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

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

fun scopeDialog(
    dialog: Dialog? = null,
    cancelable: Boolean = true,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) = DialogCoroutineScope(MainActivity.content, dialog, cancelable, dispatcher).launch(block)