package com.brookes6.cloudmusic.action

import com.brookes6.cloudmusic.ui.widget.SmartSwipeRefreshState

/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description:
 */
sealed class PlayListAction {

    class GetPlayListDetail(
        val requestIndex: Int,
        val id: Long,
        val state: SmartSwipeRefreshState?
    ) : PlayListAction()

    class PlaySong(val index: Int) : PlayListAction()

}