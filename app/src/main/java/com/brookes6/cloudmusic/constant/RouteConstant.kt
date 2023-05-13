package com.brookes6.cloudmusic.constant

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
object RouteConstant {

    const val LOGIN = "LOGIN"

    const val LOGIN_PAGE = LOGIN + "loginPage"

    const val PHONE_LOGIN_PAGE = LOGIN + "phoneLoginPage"

    const val PHONE_CODE_PAGE = LOGIN + "phoneCodePage"

    const val PHONE_QRCODE_PAGE = LOGIN + "phoneQRCodePage"

    const val HOME = "HOME"

    const val HOME_PAGE = HOME + "homePage"

    const val HOME_SONG_PAGE = HOME + "homeSongPage"

    const val HOME_SEARCH_PAGE = HOME + "homeSearchPage"

    const val HOME_MY_PAGE = HOME + "homeMyPage"

    const val SPLASH_PAGE = "splashPage"

    const val SONG = "SONG"

    const val SEARCH = "search"

    /**
     * 热门搜索页面
     */
    const val SEARCH_HOT_PAGE = SEARCH + "hotPage"

    /**
     * 具体搜索页面
     */
    const val SEARCH_DETAIL_PAGE = SEARCH + "detailPage"

    /**
     * 歌单详情页
     */
    const val SONG_PLAY_LIST = SONG + "songPlayList"

}