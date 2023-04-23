package com.brookes6.cloudmusic.action

/**
 * Author: fuxinbo

 * Date: 2023/3/8

 * Description:
 */
sealed class HomeAction {

    /**
     * 获取每日推荐歌曲
     */
    object GetRecommendSong : HomeAction()

    /**
     * 获取推荐MV
     */
    object GetRecommendMV : HomeAction()

    /**
     * 获取最近播放歌曲
     *
     */
    object GetRecordMusic : HomeAction()
}
