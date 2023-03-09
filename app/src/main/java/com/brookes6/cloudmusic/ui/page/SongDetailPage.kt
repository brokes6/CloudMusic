package com.brookes6.cloudmusic.ui.page

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.TimeUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.lzx.starrysky.OnPlayProgressListener
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.control.RepeatMode
import com.lzx.starrysky.manager.PlaybackStage

/**
 * Author: fuxinbo

 * Date: 2023/2/24

 * Description:
 */

@Preview
@Composable
fun SongDetailPage(viewModel: MainViewModel = viewModel(), activity: LifecycleOwner? = null) {
    val interationSource = remember { MutableInteractionSource() }
    val mIsTouch = interationSource.collectIsDraggedAsState()
    var mCurrentPlayTime by remember { mutableStateOf(0L) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (cover, lyrics, function) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .placeholder(drawableResId = R.drawable.shape_round_place_holder)
                .data(viewModel.song.value?.songCover)
                .build(),
            contentDescription = stringResource(id = R.string.description),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(65.dp),
        )
        Box(
            modifier = Modifier
                .statusBarsPadding()
                .background(
                    colorResource(id = R.color.search_transparent),
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
                .constrainAs(cover) {
                    top.linkTo(parent.top, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                    bottom.linkTo(function.top, 20.dp)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }, contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = !viewModel.state.mIsShowLyric.value,
                modifier = Modifier.fillMaxSize(),
                enter = slideInVertically() + fadeIn(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                ),
                exit = slideOutVertically() + fadeOut(
                    animationSpec = tween(
                        durationMillis = 800,
                        delayMillis = 200,
                        easing = FastOutSlowInEasing
                    )
                )
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .crossfade(true)
                        .data(viewModel.song.value?.songCover)
                        .build(),
                    contentDescription = stringResource(id = R.string.description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            if (viewModel.state.mIsShowLyric.value) {
                LyricsUI(
                    modifier = Modifier.fillMaxSize(),
                    viewModel,
                    mCurrentPlayTime / 1000,
                    viewModel.lyric.value.toList(),
                ) {}
            }
        }
        IconClick(
            onClick = {
                viewModel.state.mIsShowLyric.value = !viewModel.state.mIsShowLyric.value
            },
            res = if (viewModel.state.mIsShowLyric.value) R.drawable.icon_song_detail_lyric_initiate else R.drawable.icon_song_detail_lyric_no,
            modifier = Modifier
                .statusBarsPadding()
                .constrainAs(lyrics) {
                    top.linkTo(cover.top, 25.dp)
                    end.linkTo(cover.end, 25.dp)
                },
            iconSize = 30.dp
        )
        Box(modifier = Modifier
            .background(
                colorResource(id = R.color.search_transparent),
                RoundedCornerShape(20.dp)
            )
            .constrainAs(function) {
                bottom.linkTo(parent.bottom, 20.dp)
                start.linkTo(parent.start, 20.dp)
                end.linkTo(parent.end, 20.dp)
                width = Dimension.fillToConstraints
            }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = viewModel.song.value?.songName ?: "未知歌曲",
                    fontSize = 22.sp,
                    color = Color.White,
                    modifier = Modifier.paddingTop(15.dp),
                    maxLines = 1
                )
                Text(
                    text = viewModel.song.value?.artist ?: "未知作者",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    modifier = Modifier.paddingTop(5.dp)
                )
                Box(
                    modifier = Modifier
                        .paddingTop(15.dp)
                        .height(1.dp)
                        .background(Color.DarkGray)
                        .width(120.dp)
                )
                Slider(
                    value = viewModel.state.mProgress.value, onValueChange = {
                        if (mIsTouch.value) {
                            LogUtils.d("拖动设置进度条进度:${it}")
                            viewModel.state.isInitPage2.value = false
                            viewModel.state.mResetLyric.value = !viewModel.state.mResetLyric.value
                            MusicManager.instance.seekTo(
                                ((viewModel.song.value?.duration ?: 1) * it).toLong()
                            )
                        }
                        viewModel.state.mProgress.value = it
                    },
                    interactionSource = interationSource,
                    colors = SliderDefaults.colors(
                        thumbColor = colorResource(id = R.color.white),
                        inactiveTrackColor = Color.White,
                        activeTrackColor = colorResource(id = R.color.song_author)
                    ),
                    modifier = Modifier
                        .padding(12.dp, 5.dp, 12.dp, 0.dp)
                        .fillMaxWidth()
                )
                Box(
                    modifier = Modifier
                        .padding(15.dp, 0.dp, 15.dp, 0.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = TimeUtils.timeInSecond(mCurrentPlayTime),
                        modifier = Modifier.align(Alignment.TopStart),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.white)
                    )
                    Text(
                        text = TimeUtils.timeInSecond(viewModel.song.value?.duration),
                        modifier = Modifier.align(Alignment.TopEnd),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.white)
                    )
                }
                Row(
                    modifier = Modifier
                        .padding(15.dp, 15.dp, 15.dp, 20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconClick({

                    }, modifier = Modifier, R.drawable.icon_song_detail_like_normal, 30.dp)
                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconClick(
                            {
                                viewModel.dispatch(MainAction.PreSong)
                            }, modifier = Modifier,
                            R.drawable.icon_song_detail_pre,
                            50.dp
                        )
                        IconClick(
                            {
                                viewModel.dispatch(MainAction.PlayOrPauseSong)
                            }, modifier = Modifier,
                            if (viewModel.state.mPlayStatus.value != PLAY_STATUS.PLAYING) R.drawable.icon_song_detail_play else R.drawable.icon_song_detail_pause,
                            50.dp
                        )
                        IconClick(
                            {
                                viewModel.dispatch(MainAction.NextSong)
                            }, modifier = Modifier,
                            R.drawable.icon_song_detail_next,
                            50.dp
                        )
                    }
                    IconClick(
                        {
                            viewModel.dispatch(MainAction.SwitchPlayModel)
                        }, modifier = Modifier,
                        when (MusicManager.instance.getCurrentPlayModel()) {
                            RepeatMode.REPEAT_MODE_NONE -> {
                                R.drawable.icon_song_detail_sequential
                            }
                            RepeatMode.REPEAT_MODE_ONE -> {
                                R.drawable.icon_song_detail_circulate
                            }
                            RepeatMode.REPEAT_MODE_SHUFFLE -> {
                                R.drawable.icon_song_detail_random
                            }
                            else -> {
                                R.drawable.icon_song_detail_sequential
                            }
                        },
                        30.dp
                    )
                }
            }
        }
        StarrySky.with()?.setOnPlayProgressListener(object : OnPlayProgressListener {
            override fun onPlayProgress(currPos: Long, duration: Long) {
                mCurrentPlayTime = currPos
                var progress = currPos.toFloat() / duration.toFloat()
                if (progress.isNaN()) progress = 0f
                viewModel.state.mProgress.value = progress
            }
        })
        activity?.let {
            StarrySky.with()?.playbackState()?.observe(it) { play ->
                when (play.stage) {
                    PlaybackStage.IDLE -> {
                        if (!play.isStop) {
                            viewModel.state.mProgress.value = 0f
                        }
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.NOMAL
                    }
                    PlaybackStage.SWITCH -> {
                        viewModel.dispatch(MainAction.GetCurrentSong)
                    }
                    PlaybackStage.PAUSE -> {
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.PAUSE
                    }
                    PlaybackStage.PLAYING -> {
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.PLAYING
                    }
                    PlaybackStage.ERROR -> {
                        LogUtils.e("音乐：出错 --> ${play.errorMsg}", "Song")
                    }
                }
            }
        }
        LaunchedEffect(key1 = viewModel.state.currentPlayIndex.value, block = {
            viewModel.dispatch(MainAction.GetCurrentLyric)
        })
    }
}