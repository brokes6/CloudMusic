package com.brookes6.cloudmusic.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.PlayListAction
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.widget.*
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MyViewModel
import com.brookes6.cloudmusic.vm.PlayListViewModel
import com.brookes6.repository.model.PlayListInfo

/**
 * Author: fuxinbo

 * Date: 2023/3/8

 * Description:
 */

@Preview
@Composable
fun PlayListPage(
    viewModel: MyViewModel = viewModel(),
    mPlayListInfo: PlayListInfo? = null,
    onNavController: (String) -> Unit = {}
) {
    val mState = rememberLazyListState()
    val model: PlayListViewModel = viewModel()
    val mRequestIndex = remember { mutableStateOf(0) }
    val refreshState = rememberSmartSwipeRefreshState()
    Box {
        SmartSwipeRefresh(
            modifier = Modifier.fillMaxSize(),
            state = refreshState,
            onLoadMore = {
                mRequestIndex.value += 1
                model.dispatch(
                    PlayListAction.GetPlayListDetail(
                        mRequestIndex.value,
                        mPlayListInfo?.id ?: 0L,
                        refreshState
                    )
                )
            },
            isNeedRefresh = false,
            isNeedLoadMore = model.mIsShowLoadUI.value,
            footerIndicator = {
                MyRefreshFooter(refreshState.loadMoreFlag, model.mIsShowLoadUI.value)
            }
        ) {
            LazyColumn(
                state = mState
            ) {
                item {
                    Box(
                        modifier = Modifier
                            .height(300.dp)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .crossfade(true)
                                .data(mPlayListInfo?.coverImgUrl)
                                .build(),
                            contentDescription = stringResource(id = R.string.description),
                            modifier = Modifier
                                .blur(60.dp)
                                .fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                            val (back, title, cover, name, userCover, userName) = createRefs()
                            IconClick(
                                onClick = { onNavController.invoke(AppConstant.ON_BACK) },
                                res = R.drawable.icon_play_list_back,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .constrainAs(back) {
                                        top.linkTo(parent.top, 15.dp)
                                        start.linkTo(parent.start, 15.dp)
                                    },
                                iconSize = 24.dp
                            )
                            Text(
                                text = "歌单",
                                color = Color.White,
                                fontSize = 18.sp,
                                modifier = Modifier
                                    .statusBarsPadding()
                                    .constrainAs(title) {
                                        top.linkTo(back.top)
                                        bottom.linkTo(back.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .crossfade(true)
                                    .data(mPlayListInfo?.coverImgUrl)
                                    .transformations(RoundedCornersTransformation(20f))
                                    .build(),
                                contentDescription = stringResource(id = R.string.description),
                                modifier = Modifier
                                    .size(110.dp)
                                    .constrainAs(cover) {
                                        top.linkTo(back.bottom, 30.dp)
                                        start.linkTo(parent.start, 15.dp)
                                    }
                            )
                            Text(text = mPlayListInfo?.name ?: "未知名称",
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier.constrainAs(name) {
                                    top.linkTo(cover.top, 10.dp)
                                    start.linkTo(cover.end, 10.dp)
                                }
                            )
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(mPlayListInfo?.creator?.avatarUrl)
                                    .transformations(CircleCropTransformation())
                                    .build(),
                                contentDescription = stringResource(id = R.string.description),
                                modifier = Modifier
                                    .size(24.dp)
                                    .constrainAs(userCover) {
                                        top.linkTo(name.bottom, 10.dp)
                                        start.linkTo(name.start)
                                    }
                            )
                            Text(
                                text = mPlayListInfo?.creator?.nickname
                                    ?: "未知作者",
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.constrainAs(userName) {
                                    top.linkTo(userCover.top)
                                    start.linkTo(userCover.end, 5.dp)
                                    bottom.linkTo(userCover.bottom)
                                }
                            )
                        }
                    }
                }
                item {
                    Text(
                        "歌单",
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp, 25.dp, 0.dp, 0.dp)
                    )
                }
                itemsIndexed(model.playList, key = { _, item ->
                    item.id
                }) { index, item ->
                    PlayListItem(index = index, item = item) { position ->
                        model.dispatch(PlayListAction.PlaySong(position))
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
        val mIsShowTitle by remember {
            derivedStateOf {
                mState.firstVisibleItemIndex > 0
            }
        }
        AnimatedVisibility(
            visible = mIsShowTitle, modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(mainBackground)
                .statusBarsPadding(),
            enter = fadeIn(tween(durationMillis = 500, easing = LinearEasing)),
            exit = fadeOut(tween(durationMillis = 500, easing = LinearEasing))
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconClick(
                    onClick = {
                        onNavController.invoke(AppConstant.ON_BACK)
                    }, res = R.drawable.icon_play_list_back,
                    modifier = Modifier.paddingStart(15.dp),
                    iconSize = 24.dp
                )
                Text(
                    text = mPlayListInfo?.name ?: "",
                    color = Color.White,
                    fontSize = 18.sp,
                    modifier = Modifier
                        .paddingStart(10.dp)
                )
            }
        }
    }
    LaunchedEffect(key1 = mPlayListInfo?.id, block = {
        model.dispatch(
            PlayListAction.GetPlayListDetail(
                mRequestIndex.value,
                mPlayListInfo?.id ?: 0L,
                null
            )
        )
    })
    BackHandler(true) {
        onNavController.invoke(AppConstant.ON_BACK)
    }
}