package com.brookes6.cloudmusic.action

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */
sealed class MyAction{

    object GetUserInfo : MyAction()

    object GetUserPlayList : MyAction()

}
