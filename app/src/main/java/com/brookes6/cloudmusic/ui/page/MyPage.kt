package com.brookes6.cloudmusic.ui.page

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.brookes6.cloudmusic.MainActivity
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.ui.sub.MyFunctionSub
import com.brookes6.cloudmusic.ui.sub.MyLikeSongSub
import com.brookes6.cloudmusic.ui.sub.MyTopUserSub
import com.brookes6.cloudmusic.vm.MyViewModel

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */

@Composable
fun MyPage(
    viewModel: MyViewModel,
    onNavController: (String) -> Unit = {}
) {
    val state = rememberScrollState()
    Box() {
        // 背景
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(viewModel.user.value?.profile?.backgroundUrl)
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clip(RoundedCornerShape(bottomEnd = 150.dp, bottomStart = 150.dp)),
            contentScale = ContentScale.Crop
        )
        // 整体布局
        Column(
            modifier = Modifier.verticalScroll(state)
        ) {
            // 头像部分
            MyTopUserSub(viewModel, onNavController)
            // 功能区部分
            MyFunctionSub(viewModel, onNavController)
            // 我喜欢的歌单部分
            MyLikeSongSub(viewModel, onNavController)
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        viewModel.dispatch(MyAction.GetUserInfo)
        viewModel.dispatch(MyAction.GetUserPlayList)
    })
    BackHandler(enabled = true) {
        MainActivity.content.finish()
    }
}