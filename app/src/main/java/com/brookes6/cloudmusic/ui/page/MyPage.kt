package com.brookes6.cloudmusic.ui.page

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import coil.compose.AsyncImage
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.MainActivity
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.sub.MyFunctionSub
import com.brookes6.cloudmusic.ui.sub.MyLikeSongSub
import com.brookes6.cloudmusic.ui.sub.MyTopUserSub
import com.brookes6.cloudmusic.ui.theme.secondaryBackground80Percent
import com.brookes6.cloudmusic.vm.MyViewModel
import com.brookes6.cloudmusic.vm.UserViewModel
import com.brookes6.repository.model.PlayListInfo
import com.brookes6.repository.model.PlayListModel
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup

/**
 * Author: fuxinbo

 * Date: 2023/3/3

 * Description:
 */

@Composable
fun MyPage(
    viewModel: MyViewModel,
    userVM : UserViewModel,
    onNavController: (String) -> Unit = {}
) {
    val state = rememberScrollState()
    Box(modifier = Modifier.fillMaxSize()) {
        // 背景
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(userVM.user.observeAsState().value?.profile?.backgroundUrl)
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
            modifier = Modifier
                .verticalScroll(state)
                .fillMaxSize()
        ) {
            // 头像部分
            MyTopUserSub(viewModel, userVM,onNavController)
            // 功能区部分
            MyFunctionSub(viewModel, onNavController)
            // 我喜欢的歌单部分
            MyLikeSongSub(viewModel.userLikePlayList.value, onNavController)
            Box(
                modifier = Modifier
                    .padding(20.dp, 20.dp, 20.dp, 0.dp)
                    .fillMaxWidth()
                    .background(secondaryBackground80Percent, RoundedCornerShape(20.dp))
            ) {
                AndroidMyLikeSongRecyclerView(list = viewModel.playList.value, onNavController)
            }
            Spacer(modifier = Modifier.height(150.dp))
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

@Composable
fun AndroidMyLikeSongRecyclerView(list: PlayListModel?, onNavController: (String) -> Unit = {}) {
    AndroidView(
        factory = { context ->
            RecyclerView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                linear().setup {
                    addType<PlayListInfo>(R.layout.item_like_play_list)
                    onBind {
                        getModel<PlayListInfo>(modelPosition).let {
                            findView<ImageView>(R.id.imageCover).load(
                                it.coverImgUrl, builder = {
                                    crossfade(true)
                                    transformations(RoundedCornersTransformation(20f))
                                }
                            )
                            findView<TextView>(R.id.textName).text = it.name
                            findView<TextView>(R.id.textCont).text = "${it.trackCount}首"
                        }
                    }
                    onClick(R.id.roots) {
                        onNavController.invoke(RouteConstant.SONG_PLAY_LIST + "/${modelPosition}")
                    }
                }
            }.also {
                ViewCompat.setNestedScrollingEnabled(it, true)
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        it.models = list?.playlist
    }
}