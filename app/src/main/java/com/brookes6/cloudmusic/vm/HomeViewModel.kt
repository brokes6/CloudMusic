package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import androidx.lifecycle.viewModelScope
import com.brookes6.cloudmusic.action.HomeAction
import com.brookes6.cloudmusic.constant.AppConstant.USER_ID
import com.brookes6.cloudmusic.extensions.checkCookie
import com.brookes6.cloudmusic.extensions.request
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.HomeState
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.*
import com.drake.net.Get
import com.drake.net.Post
import com.drake.net.utils.withMain
import com.drake.serialize.serialize.serialize
import com.lzx.starrysky.SongInfo
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author fuxinbo
 * @Date 2023/1/16 11:33
 * @Description TODO
 */
class HomeViewModel : ViewModel() {

    private val _userInfo: MutableState<LoginModel?> = mutableStateOf(null)
    val userInfo: State<LoginModel?> = _userInfo

    private val _recommendSong: MutableState<List<SongInfo>> = mutableStateOf(listOf())
    val recommendSong: State<List<SongInfo>> = _recommendSong

    private val _mv: MutableState<RecommendMvInfo?> = mutableStateOf(null)
    val mv: State<RecommendMvInfo?> = _mv

    private val _recordMusic: MutableState<List<RecordMusicList?>> = mutableStateOf(listOf())
    val recordMusic: State<List<RecordMusicList?>> = _recordMusic

    var state by mutableStateOf(HomeState())
        private set

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: HomeAction) {
        when (action) {
            is HomeAction.GetUserInfo -> getUserInfo()
            is HomeAction.GetRecommendSong -> getRecommendSong()
            is HomeAction.GetRecommendMV -> getRecommendMV()
            is HomeAction.GetRecordMusic -> getRecordMusic()
        }
    }

    fun getRecordSong(index: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { coroutineContext, throwable ->
            LogUtils.e("歌单音乐转换异常 --> ${throwable.message}", "Song")
        }) {
            val list = mutableListOf<SongInfo>()
            _recordMusic.value.forEachIndexed { index, recordMusicList ->
                list.add(
                    SongInfo(
                        songId = "${index + 1}",
                        songName = recordMusicList?.data?.name ?: "",
                        artist = recordMusicList?.data?.ar?.getOrNull(0)?.name ?: "未知艺术家",
                        songCover = recordMusicList?.data?.al?.picUrl ?: "",
                        duration = recordMusicList?.data?.dt ?: 0,
                        id = recordMusicList?.data?.id ?: 0,
                        index = index + 1
                    )
                )
            }.also {
                MusicManager.instance.setPlayListAndPlay(index, list)
            }
        }
    }

    private fun getUserInfo() {
        request({
            DataBaseManager.db?.userDao?.getUserInfo()
        }, {
            LogUtils.i("获取本地用户数据成功:${it?.account?.userName},用户ID为 --> ${it?.account?.id}", "DAO")
            _userInfo.value = it
            serialize(USER_ID to it?.account?.id)
        }, {
            LogUtils.e("获取用户数据出现异常！->${it}")
        })
    }

    private fun getRecommendSong() {
        if (_recommendSong.value.isNotEmpty()) {
            LogUtils.i("缓存中还有数据，则不进行网络请求，使用缓存数据:${_recommendSong.value.size}")
            return
        }
        val songMap = hashMapOf<Long, SongInfo>()
        val musicId = StringBuffer()
        scopeDialog(dispatcher = Dispatchers.IO) {
            Get<RecommendSongModel>(Api.RECOMMEND_SONG).await().also {
                checkCookie(it.code) { getRecommendSong() }
                it.dailySongs?.forEachIndexed { index, recommendSong ->
                    musicId.append(if (musicId.isEmpty()) "${recommendSong.id}" else ",${recommendSong.id}")
                    songMap[recommendSong.id] = SongInfo(
                        "${index + 1}",
                        "",
                        recommendSong.name,
                        recommendSong.ar?.getOrNull(0)?.name ?: "未知艺术家",
                        recommendSong.al?.picUrl ?: "",
                        id = recommendSong.id,
                        index = index + 1
                    )
                }.also {
                    Get<List<SongModel>>(Api.GET_MUSIC_URL) {
                        param("id", musicId.toString(), true)
                        param("level", "exhigh")
                    }.await().also { song ->
                        song.forEachIndexed { index, songInfo ->
                            songMap[songInfo.id]?.run {
                                songUrl = songInfo.url
                                duration = songInfo.time
                            }
                        }.also {
                            withMain {
                                _recommendSong.value = songMap.values.toMutableList().apply {
                                    add(0, SongInfo())
                                    add(0, SongInfo())
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getRecommendMV() {
        if (_mv.value != null) return
        scopeDialog {
            Post<RecommendMvModel>(Api.GET_RECOMMEND_MV).await().also {
                if (it.code == 200) {
                    if (it.result.isEmpty()) return@scopeDialog
                    getMvUrl(it.result[0])
                }
            }
        }
    }

    private fun getMvUrl(mvData: RecommendMvInfo?) {
        if (mvData?.id == null) return
        scopeNetLife {
            Post<MvUrlModel>(Api.GET_MV_URL) {
                param("id", mvData.id)
            }.await().also {
                if (it.code == 200) {
                    mvData.url = it.data?.url ?: ""
                    _mv.value = mvData
                } else {
                    LogUtils.e("Mv地址获取失败!,id为 --> ${mvData.id}")
                }
            }
        }
    }

    private fun getRecordMusic() {
        if (_recordMusic.value.isNotEmpty()) return
        scopeDialog {
            Post<RecordMusicModel>(Api.GET_RECORD_MUSIC) {
                param("limit", 3)
            }.await().also {
                if (it.code == 200) {
                    it.data?.list?.let { list ->
                        _recordMusic.value = list
                    }
                } else {
                    LogUtils.e("获取最近歌曲失败 --> ${it.message}")
                }
            }
        }
    }
}