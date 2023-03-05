package com.brookes6.cloudmusic.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import androidx.annotation.StyleRes
import com.brookes6.cloudmusic.R

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */
class LoadingDialog @JvmOverloads constructor(
    context: Context,
    @StyleRes themeResId: Int = R.style.LoadingDialog,
) : Dialog(context, themeResId) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        initDialog()
    }

    private fun initDialog() {
        context.let {
            val dialogWindow: Window? = this.window
            dialogWindow?.setGravity(Gravity.CENTER)
            val lp: WindowManager.LayoutParams? = dialogWindow?.attributes
            lp?.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp?.width = WindowManager.LayoutParams.WRAP_CONTENT
            dialogWindow?.setAttributes(lp)
        }
    }
}