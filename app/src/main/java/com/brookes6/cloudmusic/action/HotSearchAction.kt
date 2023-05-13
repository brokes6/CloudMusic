package com.brookes6.cloudmusic.action

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
sealed class HotSearchAction {

    object Search : HotSearchAction()

    class SearchHot(val searchHotText: String?) : HotSearchAction()

    object GetSearchHotDetail : HotSearchAction()

}