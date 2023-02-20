package com.brookes6.cloudmusic.ui.page

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.brookes6.cloudmusic.ui.widget.SongItem
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.lzx.starrysky.SongInfo
import com.skydoves.landscapist.glide.GlideImage

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */

@Preview
@Composable
fun HomePage(
    navController: NavController? = null,
    viewModel: HomeViewModel = viewModel(),
    mainViewModel: MainViewModel = viewModel()
) {
    var searchText by remember { mutableStateOf("") }
    val lazyListState = rememberLazyListState()
    var lastFirstVisibleItemIndex by remember { mutableStateOf(lazyListState.firstVisibleItemIndex) }
    if (lazyListState.isScrollInProgress) {
        //进入组合后只会启动一次
        DisposableEffect(Unit) {
            LogUtils.d("开始滑动")
            onDispose {
                LogUtils.d("停止滑动")
            }
        }
        run {
            val currentFirstVisibleItemIndex = lazyListState.firstVisibleItemIndex
            //第一个可见的item改变了
            if (currentFirstVisibleItemIndex != lastFirstVisibleItemIndex) {
                //更新记录的值，退出run代码块
                lastFirstVisibleItemIndex = currentFirstVisibleItemIndex
                return@run
            }
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (bg, search, recommendTitle, recommend, video, music) = createRefs()
        // bg
        Image(
            bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_home_top_bg), null,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(bg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                },
            contentScale = ContentScale.Crop
        )
        // search
        BasicTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .constrainAs(search) {
                    top.linkTo(parent.top, 42.dp)
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                    width = Dimension.fillToConstraints
                },
            textStyle = TextStyle(fontSize = 20.sp, color = titleColor),
            maxLines = 1
        ) { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        colorResource(id = R.color.search_transparent),
                        RoundedCornerShape(20.dp)
                    )
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 24.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    if (searchText.isEmpty()) {
                        Text(text = "搜索歌曲", fontSize = 20.sp, color = titleColor)
                    } else {
                        innerTextField()
                    }
                }
                GlideImage(
                    imageModel = { viewModel.userInfo.value?.profile?.avatarUrl },
                    modifier = Modifier
                        .padding(0.dp, 20.dp, 0.dp, 20.dp)
                        .clip(CircleShape)
                        .size(32.dp)
                )
            }
        }
        Text(
            "每日推荐",
            fontSize = 18.sp,
            color = titleColor,
            modifier = Modifier
                .rotate(-90f)
                .constrainAs(recommendTitle) {
                    start.linkTo(parent.start)
                    top.linkTo(recommend.top)
                    bottom.linkTo(recommend.bottom)
                })

        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .height(168.dp)
                .constrainAs(recommend) {
                    top.linkTo(search.bottom, 35.dp)
                    start.linkTo(recommendTitle.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            horizontalArrangement = Arrangement.spacedBy(15.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            itemsIndexed(viewModel.recommendSong.value,
                key = { _, item ->
                    item.songId
                }) { index, item ->
                LogUtils.d("LazyRow发生重组 : ${index},头部index:${lastFirstVisibleItemIndex}")
                ChangerItems(index, lastFirstVisibleItemIndex, item) {
                    mainViewModel.dispatch(MainViewModel.MainAction.PlaySong(index))
                }
            }
        }
    }
    LaunchedEffect(key1 = mainViewModel.state.isLogin, block = {
        viewModel.dispatch(HomeViewModel.HomeAction.GetUserInfo)
        viewModel.dispatch(HomeViewModel.HomeAction.GetRecommendSong)
    })
}

@Composable
fun ChangerItems(
    currentIndex: Int,
    firstIndex: Int,
    item: SongInfo,
    callback: (index: Int) -> Unit = {}
) {
    val animateSize by animateDpAsState(targetValue = if (firstIndex == currentIndex) 168.dp else 136.dp)
    SongItem(
        modifier = Modifier
            .size(animateSize)
            .clickable {
                callback.invoke(currentIndex)
            }, item
    )
}