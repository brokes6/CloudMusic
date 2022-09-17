package com.cloudmusic.lib_base.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cloudmusic.lib_base.extensions.getVmClazz
import com.cloudmusic.lib_base.model.EXCEPTION_MODE
import com.cloudmusic.lib_base.utils.ViewBindingUtil
import com.drake.tooltip.dialog.BubbleDialog
import java.lang.ref.SoftReference

/**
 * Author: 付鑫博
 * Date: 2021/9/28
 * Description:
 */
abstract class BaseFragment<VM : BaseViewModel, VB : ViewDataBinding> : Fragment() {

    /**
     * 是否已经加载过数据
     */
    private var mIsLoaded: Boolean = false

    /**
     * 当前页面绑定的DataBinding
     */
    protected open val binding: VB by lazy {
        ViewBindingUtil.create(javaClass, LayoutInflater.from(context))
    }

    /**
     * Loading弹窗
     */
    private val mLoading: SoftReference<BubbleDialog> by lazy {
        SoftReference(BubbleDialog(requireContext(),
            "正在加载中"))
    }

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        createObserve()
        lazyLoadData()
        if (isLoading()) {
            createLoadingObserve()
        }
    }

    override fun onResume() {
        super.onResume()
        if (!mIsLoaded && !isHidden) {
            lazyLoadData()
        }
    }

    override fun onStop() {
        super.onStop()
        mLoading.get()?.dismiss()
    }

    abstract fun initView()

    abstract fun initData()

    open fun createObserve(){}

    /**
     * 是否开启Loading效果
     */
    open fun isLoading(): Boolean = true

    /**
     * 异常处理类
     *
     * @param exceptionStatus 异常状态(默认为-1)
     * @see EXCEPTION_MODE 各种异常对应的Int值
     */
    open fun exceptionHandling(exceptionStatus: Int = -1) {}

    private fun createLoadingObserve() {
        mViewModel.mUiChange.run {
            showLoadingDialog.observe(viewLifecycleOwner) {
                mLoading.get()?.show()
            }
            dismissLoadingDialog.observe(viewLifecycleOwner) {
                mLoading.get()?.dismiss()
            }
            showErrorView.observe(viewLifecycleOwner) {
                exceptionHandling(EXCEPTION_MODE.DATA_ERROR)
            }
            showEmptyView.observe(viewLifecycleOwner) {
                exceptionHandling(EXCEPTION_MODE.DATA_EMPTY)
            }
        }
    }

    private fun lazyLoadData() {
        mIsLoaded = true
        initData()
    }
}