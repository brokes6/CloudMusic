package com.brookes6.cloudmusic.action

/**
 * Author: fuxinbo

 * Date: 2023/3/8

 * Description:
 */
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
