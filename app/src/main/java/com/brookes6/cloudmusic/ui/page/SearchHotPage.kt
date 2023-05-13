package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.brookes6.cloudmusic.action.HotSearchAction
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.brookes6.cloudmusic.vm.SearchViewModel

/**
 * Author: Sakura

 * Date: 2023/5/12

 * Description:
 */
@Composable
fun SearchHotPage(
    nav: NavHostController,
    mSearchVM: SearchViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            "Trending",
            fontSize = 18.sp,
            color = titleColor,
            modifier = Modifier
                .padding(0.dp, 39.dp, 0.dp, 0.dp)
                .rotate(-90f)
        )
        LazyColumn(
            modifier = Modifier
                .paddingEnd(14.dp)
                .weight(1f)
                .fillMaxHeight()
        ) {
            itemsIndexed(mSearchVM.searchVO.value.mSearchHotDetailData.value) { index, item ->
                SearchHotItem(index, item) {
                    mSearchVM.dispatch(HotSearchAction.SearchHot(item?.searchWord))
                    nav.navigate(RouteConstant.SEARCH_DETAIL_PAGE)
                }
            }
            item {
                Spacer(modifier = Modifier.navigationBarsPadding())
            }
        }
    }
}