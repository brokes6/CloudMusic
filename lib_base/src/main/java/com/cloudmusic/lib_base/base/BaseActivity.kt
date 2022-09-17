package com.cloudmusic.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.cloudmusic.lib_base.extensions.getVmClazz
import com.cloudmusic.lib_base.model.EXCEPTION_MODE
import com.cloudmusic.lib_base.utils.ViewBindingUtil
import com.drake.tooltip.dialog.BubbleDialog
import java.lang.ref.SoftReference

/**
 * Author: 付鑫博

 * Date: 2021/8/10

 * Description:
 */
abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> :
    AppCompatActivity() {

    /**
     * 当前页面绑定的viewModel
     */
    protected val mViewModel: VM by lazy {
        ViewModelProvider(this).get(
            getVmClazz(
                this
            )
        )
    }

    /**
     * 当前页面绑定的DataBinding
     */
    protected open val binding: VB by lazy {
        ViewBindingUtil.create(javaClass, LayoutInflater.from(this))
    }

    /**
     * Loading弹窗
     */
    private val mLoading: SoftReference<BubbleDialog> by lazy {
        SoftReference(
            BubbleDialog(
                this,
                "正在加载中"
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init(savedInstanceState)
        initData()
    }

    private fun init(savedInstanceState: Bundle?) {
        initView(savedInstanceState)
        createObserve()
        if (isLoading()) {
            createLoadingObserve()
        }
    }

    /**
     * View相关初始化
     *
     * @param savedInstanceState
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 获取界面数据
     *
     */
    abstract fun initData()

    override fun onStop() {
        mLoading.get()?.dismiss()
        super.onStop()
    }

    /**
     * 数据观察者
     *
     */
    open fun createObserve() {}

    open fun isLoading(): Boolean = true

    /**
     * 异常处理类
     *
     * @param exceptionStatus 异常状态(默认为-1)
     * @see EXCEPTION_MODE 各种异常对应的Int值
     */
    open fun exceptionHandling(exceptionStatus: Int = -1) {}

    fun show() {
        if (mLoading.get()?.isShowing == false) {
            mLoading.get()?.show()
        }
    }

    fun hide() {
        mLoading.get()?.dismiss()
    }

    private fun createLoadingObserve() {
        mViewModel.mUiChange.run {
            showLoadingDialog.observe(this@BaseActivity) {
                mLoading.get()?.show()
            }
            dismissLoadingDialog.observe(this@BaseActivity) {
                mLoading.get()?.dismiss()
            }
            showErrorView.observe(this@BaseActivity) {
                exceptionHandling(EXCEPTION_MODE.DATA_ERROR)
            }
            showEmptyView.observe(this@BaseActivity) {
                exceptionHandling(EXCEPTION_MODE.DATA_EMPTY)
            }
        }
    }

}