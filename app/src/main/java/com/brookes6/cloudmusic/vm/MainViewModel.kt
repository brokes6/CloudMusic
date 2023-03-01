package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.bean.BottomTabBean
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.toast
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.MainState
import com.brookes6.cloudmusic.state.PAGE_TYPE
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.TimeUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LyricModel
import com.brookes6.repository.model.Lyrics
import com.drake.net.Post
import com.lzx.starrysky.SongInfo
import com.lzx.starrysky.StarrySky
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

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

    private val _lyric: MutableState<MutableList<Lyrics?>> = mutableStateOf(mutableListOf())
    val lyric: State<MutableList<Lyrics?>> = _lyric

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
            is MainAction.GetCurrentLyric -> getCurrentLyric()
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
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            toast("当前音乐列表为空,请检查网络状态")
            LogUtils.e("无法获取到当前播放音乐信息，请检查是否成功播放 --> ${throwable.message}")
        }) {
            StarrySky.with()?.let {
                _song.value = it.getPlayList()[it.getNowPlayingIndex()]
            }
        }
    }

    private fun goHomePage() {
        state.isShowBottomTab.value = true
        state.goPageType.value = PAGE_TYPE.HOME
    }

    private fun goLoginPage() {
        state.goPageType.value = PAGE_TYPE.LOGIN
    }

    private fun playSong(index: Int, list: MutableList<SongInfo>) {
        viewModelScope.launch {
            LogUtils.d("开始播放:${index}")
            MusicManager.instance.playMusicByIndex(list, index)
            state.currentPlayIndex.value = index
            state.isShowSongController.value = true
            LogUtils.i(
                "当前播放的歌曲信息为 --> \n" +
                        "ID:${list[index].songId}\n" +
                        "名称:${list[index].songName}\n" +
                        "作者:${list[index].artist}\n" +
                        "封面:${list[index].songCover}\n" +
                        "播放地址:${list[index].songUrl}\n" +
                        "时长:${list[index].duration}"
            )
        }
    }

    private fun changerSongDetailPage() {
        state.isShowSongDetailPage.value = !state.isShowSongDetailPage.value
    }

    private fun nextSong() {
        MusicManager.instance.next()
    }

    private fun preSong() {
        MusicManager.instance.pre()
    }

    private fun playOrPause() {
        StarrySky.with()?.let {
            if (it.isPaused()) {
                it.restoreMusic()
            } else {
                it.pauseMusic()
            }
        }
    }

    /**
     * 获取当前播放歌曲的歌词
     *
     */
    private fun getCurrentLyric() {
        if (song.value?.id == 0L || song.value?.id == null) return
        LogUtils.i("准备获取歌曲Id为:${song.value?.id}的歌词")
        _lyric.value.clear()
        scopeNetLife {
            Post<LyricModel>(Api.GET_MUSIC_LYRIC) {
                param("id", song.value?.id)
            }.await().also { list ->
                list.lrc?.lyric?.split("\n")?.forEach {
                    if (it.length <= 10) return@forEach
                    val lyric = Lyrics(
                        TimeUtils.dateTransformTime(it.substring(1, 6)),
                        it.substring(11, it.length)
                    )
                    _lyric.value.add(lyric)
                }
            }
        }
    }
}