package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.bean.BottomTabBean
import com.brookes6.cloudmusic.state.MainState

/**
 * @Author fuxinbo
 * @Date 2023/1/15 15:41
 * @Description TODO
 */
class MainViewModel : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: MainAction) {
        when (action) {
            is MainAction.GoHomePage -> goHomePage()
        }
    }

    fun getHomeBottomTabList(): MutableList<BottomTabBean> = mutableListOf(
        BottomTabBean("", "主页", R.mipmap.ic_home_normal, R.mipmap.ic_home_select),
        BottomTabBean("", "歌单", R.mipmap.ic_song_normal, R.mipmap.ic_song_select),
        BottomTabBean("", "搜索", R.mipmap.ic_search_normal, R.mipmap.ic_search_normal),
    )

    private fun goHomePage() {
        state.isShowBottomTab.value = true
        state.isShowSongController.value = true
        state.isShowHomePage.value = true
    }


    sealed class MainAction {
        object GoHomePage : MainAction()
    }
}