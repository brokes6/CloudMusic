package com.brookes6.cloudmusic.vm

import androidx.lifecycle.ViewModel
import com.brookes6.cloudmusic.action.PlayListAction
import com.brookes6.cloudmusic.extensions.scopeDialog
import com.brookes6.net.api.Api
import com.brookes6.repository.model.PlayListDetailModel
import com.drake.net.Post

/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description:
 */
class PlayListViewModel : ViewModel() {

    /**
     * 分发事件
     *
     * @param action 事件
     */
    fun dispatch(action: PlayListAction) {
        when (action) {
            is PlayListAction.GetPlayListDetail -> getPlayListDetail(action.id)
        }
    }

    private fun getPlayListDetail(id: Long) {
        if (id <= 0) return
        scopeDialog {
            Post<PlayListDetailModel>(Api.GET_PLAY_LIST_DETAIL) {
                param("id", id)
                // TODO 以下内容需要更改
                param("limit", 10)
                param("offset", 0)
            }.await().also {

            }
        }
    }
}