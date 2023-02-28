package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.lzx.starrysky.OnPlayProgressListener
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.manager.PlaybackStage

/**
 * Author: fuxinbo

 * Date: 2023/2/24

 * Description:
 */

@Composable
fun SongDetailPage(viewModel: MainViewModel = viewModel(), activity: LifecycleOwner? = null) {
    val interationSource = remember { MutableInteractionSource() }
    val mIsTouch = interationSource.collectIsDraggedAsState()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val (cover, name, author, lyrics, function, progress, play, next, pre) = createRefs()
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
                .blur(60.dp),
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(viewModel.song.value?.songCover)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(231.dp)
                .constrainAs(cover) {
                    top.linkTo(parent.top, 91.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
        Text(
            text = viewModel.song.value?.songName ?: "未知歌曲",
            fontSize = 22.sp,
            color = Color.White,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(cover.bottom, 28.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            maxLines = 1
        )
        Text(
            text = viewModel.song.value?.artist ?: "未知作者",
            fontSize = 12.sp,
            color = colorResource(id = R.color.song_author),
            modifier = Modifier.constrainAs(author) {
                top.linkTo(name.bottom, 12.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
        Slider(value = viewModel.state.mProgress.value, onValueChange = {
            if (mIsTouch.value) {
                MusicManager.instance.seekTo(
                    ((viewModel.song.value?.duration ?: 1) * it).toLong()
                )
            }
            viewModel.state.mProgress.value = it
        },
            interactionSource = interationSource,
            colors = SliderDefaults.colors(
                thumbColor = colorResource(id = R.color.song_author),
                inactiveTrackColor = Color.White,
                activeTrackColor = colorResource(id = R.color.song_author)
            ),
            modifier = Modifier
                .constrainAs(progress) {
                    start.linkTo(parent.start, 24.dp)
                    end.linkTo(parent.end, 24.dp)
                    bottom.linkTo(play.top, 58.dp)
                    width = Dimension.fillToConstraints
                }
        )
        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.PlayOrPauseSong)
        }, modifier = Modifier.constrainAs(play) {
            bottom.linkTo(parent.bottom, 43.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_song_detail_play),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(75.dp),
                tint = Color.Unspecified,
            )
        }
        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.PreSong)
        }, modifier = Modifier.constrainAs(pre) {
            top.linkTo(play.top)
            bottom.linkTo(play.bottom)
            end.linkTo(play.start, 33.dp)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_song_detail_pre),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(24.dp),
                tint = Color.Unspecified,
            )
        }
        IconButton(onClick = {
            viewModel.dispatch(MainViewModel.MainAction.NextSong)
        }, modifier = Modifier.constrainAs(next) {
            top.linkTo(play.top)
            bottom.linkTo(play.bottom)
            start.linkTo(play.end, 33.dp)
        }) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_song_detail_next),
                contentDescription = stringResource(id = R.string.description),
                modifier = Modifier
                    .size(24.dp),
                tint = Color.Unspecified,
            )
        }
        StarrySky.with()?.setOnPlayProgressListener(object : OnPlayProgressListener {
            override fun onPlayProgress(currPos: Long, duration: Long) {
                viewModel.state.mProgress.value = currPos.toFloat() / duration.toFloat()
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