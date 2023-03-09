package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.PlayListAction
import com.brookes6.cloudmusic.bean.ScrollStrategy
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.widget.CollapsingToolbarScaffold
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.ui.widget.rememberCollapsingToolbarScaffoldState
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
    playListIndex: Int = 0,
    viewModel: MyViewModel = viewModel(),
    onNavController: (String) -> Unit = {}
) {
    val model: PlayListViewModel = viewModel()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = rememberCollapsingToolbarScaffoldState(),
        scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
        toolbar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(357.dp)
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .crossfade(true)
                        .data(viewModel.playList.value?.playlist?.get(playListIndex)?.coverImgUrl)
                        .build(),
                    contentDescription = stringResource(id = R.string.description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    mainBackground
                                )
                            )
                        )
                )
                IconClick(
                    onClick = {
                        onNavController.invoke(AppConstant.ON_BACK)
                    }, res = R.drawable.icon_play_list_back,
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(20.dp, 10.dp, 0.dp, 0.dp),
                    iconSize = 24.dp
                )
                Column(
                    modifier = Modifier
                        .padding(24.dp, 0.dp, 0.dp, 30.dp)
                        .align(Alignment.BottomStart)
                ) {
                    Text(
                        text = viewModel.getPlayList(playListIndex)?.name ?: "",
                        color = Color.White,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(9.dp))
                    Text(
                        text = viewModel.getPlayList(playListIndex)?.description ?: "没有留下介绍～",
                        color = colorResource(id = R.color.song_author),
                        fontSize = 12.sp
                    )
                }
            }
        }) {
        Column() {
            Text(
                "歌单",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(20.dp, 46.dp, 0.dp, 0.dp)
                    .rotate(-90f)
            )
        }
    }
    LaunchedEffect(key1 = Unit, block = {
        model.dispatch(
            PlayListAction.GetPlayListDetail(
                viewModel.getPlayList(playListIndex)?.id ?: 0
            )
        )
    })
}

fun MyViewModel.getPlayList(index: Int): PlayListInfo? = playList.value?.playlist?.get(index)