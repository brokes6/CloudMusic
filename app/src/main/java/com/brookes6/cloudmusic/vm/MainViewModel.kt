package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.bean.BottomTabBean
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.MainState
import com.brookes6.cloudmusic.state.PAGE_TYPE
import com.brookes6.cloudmusic.utils.LogUtils
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky

/**
 * @Author fuxinbo
 * @Date 2023/1/15 15:41
 * @Description TODO
 */
class MainViewModel : ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    private val _song: MutableState<SongInfo?> = mutableStateOf(null)
    val song: State<SongInfo?> = _song

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: MainAction) {
        when (action) {
            is MainAction.GoHomePage -> goHomePage()
            is MainAction.GoLoginPage -> goLoginPage()
            is MainAction.PlaySong -> playSong(action.index, action.list)
            is MainAction.GetCurrentSong -> getCurrentSongInfo()
            is MainAction.ChangerSongDetailPage -> changerSongDetailPage()
            is MainAction.NextSong -> nextSong()
            is MainAction.PreSong -> preSong()
            is MainAction.PlayOrPauseSong -> playOrPause()
        }
    }

    fun getHomeBottomTabList(): MutableList<BottomTabBean> = mutableListOf(
        BottomTabBean(
            RouteConstant.HOME_PAGE,
            "主页",
            R.mipmap.ic_home_normal,
            R.mipmap.ic_home_select
        ),
        BottomTabBean(
            RouteConstant.HOME_SONG_PAGE,
            "歌单",
            R.mipmap.ic_song_normal,
            R.mipmap.ic_song_select
        ),
        BottomTabBean(
            RouteConstant.HOME_SEARCH_PAGE,
            "搜索",
            R.mipmap.ic_search_normal,
            R.mipmap.ic_search_normal
        ),
    )

    private fun getCurrentSongInfo() {
        _song.value = StarrySky.with().getPlayList()[state.currentPlayIndex.value]
    }

    private fun goHomePage() {
        state.isShowBottomTab.value = true
        state.goPageType.value = PAGE_TYPE.HOME
    }

    private fun goLoginPage() {
        state.goPageType.value = PAGE_TYPE.LOGIN
    }

    private fun playSong(index: Int, list: MutableList<SongInfo>) {
        LogUtils.d("开始播放:${index}")
        state.currentPlayIndex.value = index
        state.isShowSongController.value = true
        MusicManager.instance.playOnly(list,index)
        LogUtils.i("播放Url为 --> ${list[index].songUrl}")
    }

    private fun changerSongDetailPage() {
        state.isShowSongDetailPage.value = !state.isShowSongDetailPage.value
    }

    private fun nextSong(){
        MusicManager.instance.next()
    }

    private fun preSong(){
        MusicManager.instance.pre()
    }

    private fun playOrPause(){
        StarrySky.with().let {
            if (it.isPaused()) {
                it.restoreMusic()
            } else {
                it.playMusicById(state.currentPlayIndex.value.toString())
            }
        }
    }

    sealed class MainAction {
        object GoHomePage : MainAction()

        object GoLoginPage : MainAction()

        class PlaySong(val index: Int, val list: MutableList<SongInfo>) : MainAction()

        object GetCurrentSong : MainAction()

        object ChangerSongDetailPage : MainAction()

        object NextSong : MainAction()

        object PreSong : MainAction()

        object PlayOrPauseSong : MainAction()
    }
}