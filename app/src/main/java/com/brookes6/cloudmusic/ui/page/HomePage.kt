package com.brookes6.cloudmusic.ui.page

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.compose.rememberAsyncImagePainter
import coil.load
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.MainActivity
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.HomeAction
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.brookes6.cloudmusic.ui.view.FocusLayoutManager
import com.brookes6.cloudmusic.ui.view.FocusLayoutManager.Companion.dp2px
import com.brookes6.cloudmusic.ui.widget.RecordMusicItem
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.UserViewModel
import com.brookes6.repository.model.RecommendMvInfo
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.lzx.starrysky.SongInfo
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer

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
    mainViewModel: MainViewModel = viewModel(),
    userVM: UserViewModel = viewModel()
) {
    val scrollState = rememberScrollState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .navigationBarsPadding()
    ) {
        val (bg, search, recommendTitle, recommend, videoTitle, video, recentlyMusic, music, space) = createRefs()
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
        Row(
            modifier = Modifier
                .clickable {
                    navController?.navigate(RouteConstant.HOME_SEARCH_PAGE)
                }
                .background(
                    colorResource(id = R.color.search_transparent),
                    RoundedCornerShape(20.dp)
                )
                .constrainAs(search) {
                    top.linkTo(parent.top, 42.dp)
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                    width = Dimension.fillToConstraints
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .paddingStart(20.dp)
                    .weight(1f)
            ) {
                Text(text = "搜索歌曲", fontSize = 20.sp, color = titleColor)
            }
            Image(
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(userVM.user.observeAsState().value?.profile?.avatarUrl)
                        .size(Size(32, 32))
                        .build()
                ),
                stringResource(id = R.string.description),
                modifier = Modifier
                    .padding(0.dp, 20.dp, 24.dp, 20.dp)
                    .clip(CircleShape)
                    .size(32.dp)
            )
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
        Box(
            modifier = Modifier
                .height(168.dp)
                .constrainAs(recommend) {
                    top.linkTo(search.bottom, 35.dp)
                    start.linkTo(recommendTitle.end)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }, contentAlignment = Alignment.CenterStart
        ) {
            AndroidRecyclerView(
                viewModel.recommendSong.value
            ) { index ->
                mainViewModel.dispatch(
                    MainAction.PlaySong(
                        index,
                        viewModel.recommendSong.value.toMutableList()
                    )
                )
            }
        }
        Text(
            "推荐MV",
            fontSize = 18.sp,
            color = titleColor,
            modifier = Modifier
                .rotate(-90f)
                .constrainAs(videoTitle) {
                    start.linkTo(parent.start)
                    top.linkTo(video.top)
                    bottom.linkTo(video.bottom)
                })
        Box(modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .height(168.dp)
            .constrainAs(video) {
                top.linkTo(recommend.bottom, 46.dp)
                end.linkTo(parent.end, 20.dp)
                start.linkTo(videoTitle.end, 20.dp)
                width = Dimension.fillToConstraints
            }) {
            AndroidVideoView(mv = viewModel.mv.value)
        }
        Text(
            "最近音乐",
            fontSize = 18.sp,
            color = titleColor,
            modifier = Modifier
                .rotate(-90f)
                .constrainAs(recentlyMusic) {
                    start.linkTo(parent.start)
                    top.linkTo(music.top)
                    bottom.linkTo(music.bottom)
                })
        Column(
            modifier = Modifier.constrainAs(music) {
                top.linkTo(video.bottom, 46.dp)
                end.linkTo(parent.end, 20.dp)
                start.linkTo(recentlyMusic.end, 20.dp)
                width = Dimension.fillToConstraints
            },
        ) {
            viewModel.recordMusic.value.forEachIndexed { index, item ->
                RecordMusicItem(index, item) {
                    viewModel.getRecordSong(it)
                }
            }
        }
        Spacer(modifier = Modifier
            .height(150.dp)
            .constrainAs(space) {
                top.linkTo(music.bottom)
                start.linkTo(music.start)
            })
    }
    LaunchedEffect(key1 = mainViewModel.state.isLogin, block = {
        viewModel.dispatch(HomeAction.GetRecommendSong)
        viewModel.dispatch(HomeAction.GetRecommendMV)
        viewModel.dispatch(HomeAction.GetRecordMusic)
    })
    DisposableEffect(Unit) {
        onDispose {
            GSYVideoManager.releaseAllVideos()
        }
    }
    BackHandler(enabled = true) {
        if (GSYVideoManager.backFromWindowFull(MainActivity.content)) {
            return@BackHandler
        }
        MainActivity.content.finish()
    }
}

/**
 * 由于Compose LazyRow性能过差，还是使用Android View中的RecyclerView
 * @param item 数据
 */
@Composable
fun AndroidRecyclerView(
    item: List<SongInfo>,
    modifier: Modifier = Modifier,
    onClickCallback: (index: Int) -> Unit = {}
) {
    AndroidView(factory = { context ->
        RecyclerView(context).apply {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            layoutManager = FocusLayoutManager.Builder()
                .layerPadding(dp2px(context, 15f))
                .focusOrientation(FocusLayoutManager.FOCUS_LEFT)
                .isAutoSelect(true)
                .maxLayerCount(3)
                .build()
            this.setup {
                addType<SongInfo>(R.layout.item_home_recommend_song)
                onBind {
                    getModel<SongInfo>(modelPosition).let { song ->
                        findView<ImageView>(R.id.imageCover).load(song.songCover) {
                            transformations(RoundedCornersTransformation(20f))
                        }
                    }
                }
                onClick(R.id.roots) {
                    onClickCallback.invoke(modelPosition)
                }
            }
        }
    },
        modifier = modifier,
        update = {
            it.models = item
        })
}

/**
 * 视频播放器
 *
 */
@Composable
fun AndroidVideoView(
    mv: RecommendMvInfo?
) {
    AndroidView(factory = { context ->
        StandardGSYVideoPlayer(context).apply {
            setIsTouchWiget(false)
            isReleaseWhenLossAudio = true
            backButton.visibility = View.GONE
            thumbImageViewLayout.visibility = View.VISIBLE
            fullscreenButton.setOnClickListener {
                startWindowFullscreen(context, false, true)
            }
        }
    }, modifier = Modifier.fillMaxSize(),
        update = {
            mv?.let { mv ->
                it.setUpLazy(mv.url, true, null, null, null)
                it.clearThumbImageView()
                it.thumbImageView = ImageView(MainActivity.content).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    load(mv.picUrl)
                }
                it.thumbImageView.visibility = View.VISIBLE
            }
        })
}