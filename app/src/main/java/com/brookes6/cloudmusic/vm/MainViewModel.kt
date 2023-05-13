package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.bean.BottomTabBean
import com.brookes6.cloudmusic.bean.enum.BottomDialogEnum
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.toast
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.MainState
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.TimeUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LyricModel
import com.brookes6.repository.model.Lyrics
import com.drake.net.Get
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

    private var mCurrentSongId: Long = 0

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
            is MainAction.PlaySong -> playSong(action.index, action.list)
            is MainAction.GetCurrentSong -> getCurrentSongInfo()
            is MainAction.ChangerSongDetailPage -> changerSongDetailPage()
            is MainAction.NextSong -> nextSong()
            is MainAction.PreSong -> preSong()
            is MainAction.PlayOrPauseSong -> playOrPause()
            is MainAction.GetCurrentLyric -> getCurrentLyric()
            is MainAction.SwitchPlayModel -> switchPlayModel()
            is MainAction.ShowMusicDialog -> showMusicDialog(action.type)
        }
    }

    fun getHomeBottomTabList(): MutableList<BottomTabBean> = mutableListOf(
        BottomTabBean(
            RouteConstant.HOME_PAGE,
            "主页",
            R.drawable.ic_home_normal,
            R.drawable.ic_home_select
        ),
        BottomTabBean(
            RouteConstant.HOME_SONG_PAGE,
            "歌单",
            R.drawable.ic_song_normal,
            R.drawable.ic_song_select
        ),
        BottomTabBean(
            RouteConstant.HOME_MY_PAGE,
            "我的",
            R.drawable.ic_me_normal,
            R.drawable.ic_me_select
        ),
    )

    private fun getCurrentSongInfo() {
        state.isInitPage2.value = false
        state.mResetLyric.value = !state.mResetLyric.value
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            toast("当前音乐列表为空,请检查网络状态")
            LogUtils.e("无法获取到当前播放音乐信息，请检查是否成功播放 --> ${throwable.message}", "Request")
        }) {
            StarrySky.with()?.let {
                _song.value = it.getPlayList()[it.getNowPlayingIndex()]
                LogUtils.i(
                    "准备播放的歌曲信息为 --> \n" +
                            "ID:${_song.value?.songId}\n" +
                            "名称:${_song.value?.songName}\n" +
                            "作者:${_song.value?.artist}\n" +
                            "封面:${_song.value?.songCover}\n" +
                            "播放地址:${_song.value?.songUrl}\n" +
                            "时长:${_song.value?.duration}", "Song"
                )
            }
        }
    }

    private fun playSong(index: Int, list: MutableList<SongInfo>) {
        viewModelScope.launch {
            MusicManager.instance.getPlayList().let { musicList ->
                if (list.size != musicList.size) return@let
                if (list.zip(musicList).all {
                        it.first.id == it.second.id
                    }) {
                    LogUtils.i("当前歌曲列表已经已存在,将直接播放", "Song")
                    MusicManager.instance.playOnly(index)
                    state.currentPlayIndex.value = index
                    return@launch
                }
            }
            MusicManager.instance.playMusicByIndex(list, index)
            state.currentPlayIndex.value = index
            state.isShowSongController.value = true
        }
    }

    private fun changerSongDetailPage() {
        state.isShowSongDetailPage.value = !state.isShowSongDetailPage.value
    }

    private fun nextSong() {
        MusicManager.instance.next()
        state.currentPlayIndex.value = MusicManager.instance.getPlayPosition()
    }

    private fun preSong() {
        MusicManager.instance.pre()
        state.currentPlayIndex.value = MusicManager.instance.getPlayPosition()
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
        if (song.value?.id == 0L || song.value?.id == null) {
            LogUtils.i("当前播放歌曲ID为空，无法获取歌词")
            return
        }
        if (mCurrentSongId == song.value?.id) {
            LogUtils.i("相同的歌曲，不重复获取歌词")
            return
        }
        mCurrentSongId = song.value?.id ?: 0
        LogUtils.i("准备获取歌曲Id为:${song.value?.id}的歌词")
        _lyric.value.clear()
        scopeNetLife {
            Get<LyricModel>(Api.GET_MUSIC_LYRIC) {
                param("id", song.value?.id)
                param("timestamp", System.currentTimeMillis())
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

    private fun switchPlayModel() {
        MusicManager.instance.switchPlayModel()
    }

    private fun showMusicDialog(type : BottomDialogEnum){
        state.mShowDialogType.value = type.ordinal
    }
}