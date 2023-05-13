package com.brookes6.cloudmusic.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.HotSearchAction
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.ui.theme.normalText
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.ui.widget.SearchHistoryItem
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.SearchViewModel
import com.brookes6.repository.model.SearchHotDetailInfo
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * @Author fuxinbo
 * @Date 2023/1/17 14:21
 * @Description TODO
 */

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun SearchPage(
    mainVm: MainViewModel? = null,
    nav: NavController? = null,
    mSearchVM: SearchViewModel = viewModel()
) {
    val navController = rememberAnimatedNavController()
    Column(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(20.dp, 18.dp, 20.dp, 0.dp)
                .background(
                    colorResource(id = R.color.search_bg),
                    RoundedCornerShape(40.dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
                    .background(Color.White, RoundedCornerShape(20.dp))
            ) {
                BasicTextField(
                    value = mSearchVM.searchVO.value.mSearchText.value,
                    onValueChange = {
                        mSearchVM.searchVO.value.mSearchText.value = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(20.dp)),
                    textStyle = TextStyle(fontSize = 15.sp, color = normalText),
                    maxLines = 1
                ) { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .paddingStart(20.dp)
                                .weight(1f)
                        ) {
                            if (mSearchVM.searchVO.value.mSearchText.value.isEmpty()) {
                                Text(
                                    text = "输入想要搜索的关键字",
                                    fontSize = 15.sp,
                                    color = normalText
                                )
                            } else {
                                innerTextField()
                            }
                        }
                        Box(
                            modifier = Modifier
                                .padding(0.dp, 4.dp, 4.dp, 4.dp)
                                .size(58.dp)
                                .background(
                                    colorResource(id = R.color.search_button_bg),
                                    RoundedCornerShape(17.dp)
                                )
                        ) {
                            IconClick(onClick = {
                                if (mSearchVM.searchVO.value.mSearchText.value.isEmpty()) {
                                    return@IconClick
                                }
                                mSearchVM.dispatch(HotSearchAction.Search)
                                navController.navigate(RouteConstant.SEARCH_DETAIL_PAGE)
                            }, modifier = Modifier.align(Alignment.Center), res = R.drawable.search)
                        }
                    }
                }
            }
            LazyRow(modifier = Modifier.padding(20.dp, 28.dp, 20.dp, 32.dp)) {
                items(mSearchVM.searchVO.value.mSearchTextHistory.value, key = {
                    it?.searchId ?: 0
                }) {
                    if (it != null) SearchHistoryItem(it) { searchHistory ->
                        mSearchVM.searchVO.value.mSearchText.value = searchHistory
                    }
                }
            }
        }
        AnimatedNavHost(
            navController = navController,
            startDestination = RouteConstant.SEARCH_HOT_PAGE,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            composable(RouteConstant.SEARCH_HOT_PAGE) {
                SearchHotPage(nav = navController, mSearchVM = mSearchVM)
            }
            composable(RouteConstant.SEARCH_DETAIL_PAGE) {
                SearchDetailPage(nav = nav, mSearchVM = mSearchVM)
            }
        }
    }
    LaunchedEffect(key1 = mSearchVM, block = {
        mainVm?.state?.isShowBottomTab?.value = false
        mSearchVM.dispatch(HotSearchAction.GetSearchHotDetail)
    })
    BackHandler {
        mainVm?.state?.isShowBottomTab?.value = true
        nav?.popBackStack()
    }
}

@Composable
fun SearchHotItem(
    index: Int,
    item: SearchHotDetailInfo?,
    onClickCallBack: (index: Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(0.dp, 12.5.dp, 0.dp, 12.5.dp)
            .fillMaxWidth()
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                onClickCallBack.invoke(index)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#${index + 1}", fontSize = 12.sp, color = Color.White,
            modifier = Modifier.width(35.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item?.searchWord ?: "暂无热门搜索词", fontSize = 12.sp, color = Color.White
            )
            Text(
                text = item?.content ?: "未知介绍",
                fontSize = 10.sp,
                color = colorResource(id = R.color.song_author),
                modifier = Modifier.paddingTop(2.dp)
            )
        }
        IconClick(
            onClick = {

            }, res = R.drawable.icon_item_more,
            iconSize = 24.dp,
            modifier = Modifier.paddingEnd(15.dp)
        )
    }
}