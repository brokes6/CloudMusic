package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.HotSearchAction
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.ui.widget.PagerIndicator
import com.brookes6.cloudmusic.vm.SearchViewModel
import com.brookes6.repository.model.SearchResultAlbums
import com.brookes6.repository.model.SearchResultArtists
import com.brookes6.repository.model.SearchResultPlaylists
import com.brookes6.repository.model.SearchResultSong
import com.lt.compose_views.compose_pager.ComposePager
import com.lt.compose_views.compose_pager.rememberComposePagerState

/**
 * Author: Sakura

 * Date: 2023/5/12

 * Description:
 */
// TODO
@Composable
fun SearchDetailPage(nav: NavController?, mSearchVM: SearchViewModel = viewModel()) {
    val pagerState = rememberComposePagerState()
    val tabList = listOf("歌曲", "专辑", "歌手", "歌单", "用户", "MV")
    Column(modifier = Modifier.fillMaxSize()) {
        PagerIndicator(
            texts = tabList,
            offsetPercentWithSelectFlow = remember { pagerState.createChildOffsetPercentFlow() },
            selectIndexFlow = remember { pagerState.createCurrSelectIndexFlow() },
            onIndicatorClick = { index ->
                pagerState.setPageIndexWithAnimate(index)
            },
            modifier = Modifier.padding(20.dp, 20.dp, 20.dp, 0.dp)
        )
        ComposePager(
            pageCount = tabList.size,
            modifier = Modifier
                .padding(20.dp, 20.dp, 20.dp, 20.dp)
                .fillMaxWidth()
                .weight(1f),
            composePagerState = pagerState
        ) {
            LazyColumn(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxSize()
            ) {
                when (index) {
                    0 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultSongData.value,
                            key = { index, item ->
                                item?.id ?: 0
                            }) { index, item ->
                            SearchSongResult(index, item) {

                            }
                        }
                    }

                    1 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultAlbumsData.value,
                            key = { index, item ->
                                item?.id ?: 0
                            }) { index, item ->
                            SearchAlbumsResult(index, item) {

                            }
                        }
                    }

                    2 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultArtistsData.value,
                            key = { index, item ->
                                item?.id ?: 0
                            }) { index, item ->
                            SearchArtistsResult(index, item) {

                            }
                        }
                    }

                    3 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultPlaylistsData.value,
                            key = { index, item ->
                                item?.id ?: 0
                            }) { index, item ->
                            SearchPlaylistsResult(index, item) {

                            }
                        }
                    }

                    4 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultUserData.value,
                            key = { index, item ->
                                item?.userId ?: 0
                            }) { index, item ->

                        }
                    }

                    5 -> {
                        itemsIndexed(
                            mSearchVM.searchVO.value.mSearchResultMVData.value,
                            key = { index, item ->
                                item?.id ?: 0
                            }) { index, item ->

                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }
    LaunchedEffect(key1 = pagerState.getCurrSelectIndex(), block = {
        mSearchVM.searchVO.value.mSelectSearchType = pagerState.getCurrSelectIndex()
        mSearchVM.dispatch(HotSearchAction.Search)
    })
}

/**
 * 歌曲搜索UI
 */
@Composable
private fun SearchSongResult(index: Int, item: SearchResultSong?, onClickCallBack: (Int) -> Unit) {
    item ?: return
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.al?.picUrl)
                .transformations(RoundedCornersTransformation(10f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name ?: "未知歌曲", fontSize = 12.sp, color = Color.White
            )
            Text(
                text = item.artists.getOrNull(0)?.name ?: "未知作者",
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


/**
 * 专辑搜索UI
 */
@Composable
private fun SearchAlbumsResult(
    index: Int,
    item: SearchResultAlbums?,
    onClickCallBack: (Int) -> Unit
) {
    item ?: return
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.picUrl)
                .transformations(RoundedCornersTransformation(10f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name ?: "未知专辑", fontSize = 12.sp, color = Color.White
            )
            Text(
                text = item.artists.getOrNull(0)?.name ?: "未知作者",
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


/**
 * 歌手搜索UI
 */
@Composable
private fun SearchArtistsResult(
    index: Int,
    item: SearchResultArtists?,
    onClickCallBack: (Int) -> Unit
) {
    item ?: return
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.picUrl)
                .transformations(RoundedCornersTransformation(10f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name ?: "未知专辑", fontSize = 12.sp, color = Color.White
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


/**
 * 歌单搜索UI
 */
@Composable
private fun SearchPlaylistsResult(
    index: Int,
    item: SearchResultPlaylists?,
    onClickCallBack: (Int) -> Unit
) {
    item ?: return
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item.coverImgUrl)
                .transformations(RoundedCornersTransformation(10f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.name ?: "未知专辑", fontSize = 12.sp, color = Color.White
            )
            Text(
                text = item.description ?: "",
                fontSize = 10.sp,
                color = colorResource(id = R.color.song_author),
                modifier = Modifier.paddingTop(2.dp),
                maxLines = 2
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