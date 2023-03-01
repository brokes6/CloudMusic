package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
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
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.utils.TimeUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.lzx.starrysky.OnPlayProgressListener
import com.lzx.starrysky.StarrySky
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
        val (cover, name, author, lyrics, function, startTime, endTime, progress, play, next, pre) = createRefs()
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
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(viewModel.song.value?.songCover)
                .transformations(RoundedCornersTransformation(24f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .constrainAs(cover) {
                    top.linkTo(author.bottom, 20.dp)
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 50.dp)
                    bottom.linkTo(progress.top, 45.dp)
                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = viewModel.song.value?.songName ?: "未知歌曲",
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier
                .statusBarsPadding()
                .constrainAs(name) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start, 20.dp)
                },
            maxLines = 1
        )
        Text(
            text = viewModel.song.value?.artist ?: "未知作者",
            fontSize = 12.sp,
            color = colorResource(id = R.color.song_author),
            modifier = Modifier.constrainAs(author) {
                top.linkTo(name.bottom, 12.dp)
                start.linkTo(name.start)
            }
        )
        IconClick(
            onClick = {

            },
            res = if (viewModel.state.mIsShowLyric.value) R.drawable.icon_song_detail_lyric_initiate else R.drawable.icon_song_detail_lyric_no,
            modifier = Modifier.constrainAs(lyrics) {
                end.linkTo(progress.end)
                bottom.linkTo(progress.top)
            },
            iconSize = 30.dp
        )
        Slider(value = viewModel.state.mProgress.value, onValueChange = {
            if (mIsTouch.value) {
                LogUtils.d("拖动设置进度条进度:${it}")
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
                activeTrackColor = colorResource(id = R.color.black)
            ),
            modifier = Modifier
                .constrainAs(progress) {
                    start.linkTo(parent.start, 20.dp)
                    end.linkTo(parent.end, 20.dp)
                    bottom.linkTo(play.top, 38.dp)
                    width = Dimension.fillToConstraints
                }
        )
        Text(
            text = TimeUtils.timeInSecond(mCurrentPlayTime),
            modifier = Modifier.constrainAs(startTime) {
                start.linkTo(progress.start)
                top.linkTo(progress.bottom, 3.dp)
            },
            fontSize = 12.sp,
            color = colorResource(id = R.color.white)
        )
        Text(
            text = TimeUtils.timeInSecond(viewModel.song.value?.duration),
            modifier = Modifier.constrainAs(endTime) {
                end.linkTo(progress.end)
                top.linkTo(progress.bottom, 3.dp)
            },
            fontSize = 12.sp,
            color = colorResource(id = R.color.white)
        )

        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.PlayOrPauseSong)
        }, modifier = Modifier.constrainAs(play) {
            bottom.linkTo(function.top, 20.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = if (viewModel.state.mPlayStatus.value != PLAY_STATUS.PLAYING) R.drawable.icon_song_detail_play else R.drawable.icon_song_detail_pause),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(50.dp),
                tint = Color.Unspecified,
            )
        }
        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.PreSong)
        }, modifier = Modifier.constrainAs(pre) {
            top.linkTo(play.top)
            bottom.linkTo(play.bottom)
            end.linkTo(play.start, 43.dp)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_song_detail_pre),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(40.dp),
                tint = Color.Unspecified,
            )
        }
        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.NextSong)
        }, modifier = Modifier.constrainAs(next) {
            top.linkTo(play.top)
            bottom.linkTo(play.bottom)
            start.linkTo(play.end, 43.dp)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_song_detail_next),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(40.dp),
                tint = Color.Unspecified,
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .constrainAs(function) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }, horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconClick({

            }, modifier = Modifier, R.drawable.icon_song_detail_like_normal, 30.dp)
            IconClick({

            }, modifier = Modifier, R.drawable.icon_song_detail_sequential, 30.dp)
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
                        LogUtils.i("音乐：切歌", "Song")
                        viewModel.dispatch(MainViewModel.MainAction.GetCurrentSong)
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
    }
}