package com.brookes6.cloudmusic.action

/**
 * Author: fuxinbo

 * Date: 2023/3/9

 * Description:
 */
sealed class PlayListAction {

    class GetPlayListDetail(val id : Long) : PlayListAction()

}