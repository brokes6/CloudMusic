package com.brookes6.cloudmusic.vm

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.scopeNetLife
import com.brookes6.cloudmusic.extensions.request
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.HomeState
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.brookes6.repository.model.RecommendSongModel
import com.brookes6.repository.model.SongModel
import com.drake.net.Get
import com.drake.net.Post
import com.lzx.starrysky.SongInfo

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
        }
    }

    private fun getUserInfo() {
        request({
            DataBaseManager.db?.userDao?.getUserInfo()
        }, {
            LogUtils.i("获取本地用户数据成功:${it?.account?.userName}", "DAO")
            _userInfo.value = it
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
        scopeNetLife {
            Get<RecommendSongModel>(Api.RECOMMEND_SONG).await().also {
                it.dailySongs?.forEachIndexed { index, recommendSong ->
                    musicId.append(if (musicId.isEmpty()) "${recommendSong.id}" else ",${recommendSong.id}")
                    songMap[recommendSong.id] = SongInfo(
                        "${index + 1}",
                        "",
                        recommendSong.name,
                        recommendSong.ar?.getOrNull(0)?.name ?: "未知艺术家",
                        recommendSong.al?.picUrl ?: "",
                        id = recommendSong.id,
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
                            _recommendSong.value = songMap.values.toMutableList()
                        }
                    }
                }
            }
        }
    }

    sealed class HomeAction {

        /**
         * 从数据库中获取用户信息
         */
        object GetUserInfo : HomeAction()

        /**
         * 获取每日推荐歌曲
         */
        object GetRecommendSong : HomeAction()
    }

}