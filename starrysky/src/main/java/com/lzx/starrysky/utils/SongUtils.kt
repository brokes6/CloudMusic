package com.lzx.starrysky.utils

import com.brookes6.net.api.Api
import com.brookes6.repository.model.SongModel
import com.drake.net.Get
import com.drake.net.utils.scopeNet
import com.drake.net.utils.withMain
import kotlinx.coroutines.Dispatchers

/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description:
 */
object SongUtils {

    fun getSongUrl(id: Long, onSuccess: (url: String) -> Unit) {
        scopeNet(dispatcher = Dispatchers.IO){
            Get<List<SongModel>>(Api.GET_MUSIC_URL) {
                param("id", id.toString(), true)
                param("level", "exhigh")
            }.await().also {
                withMain {
                    onSuccess.invoke(it[0].url)
                }
            }
        }
    }
}