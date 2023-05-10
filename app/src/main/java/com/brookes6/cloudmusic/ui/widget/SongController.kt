package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.bean.type.BottomDialogEnum
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.brookes6.cloudmusic.vm.MainViewModel

/**
 * @Author fuxinbo
 * @Date 2023/1/15 15:53
 * @Description TODO
 */
@Preview
@Composable
fun SongController(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel()
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.song_controller),
                RoundedCornerShape(20.dp)
            )
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                viewModel.dispatch(MainAction.ChangerSongDetailPage)
            }
            .padding(4.dp)
    ) {
        val (songImage, songName, songAuthor, songProgress, songPlayType, songPlayStatus) = createRefs()
        MusicCover(
            modifier = Modifier
                .constrainAs(songImage) {
                    top.linkTo(parent.top, 4.dp)
                    bottom.linkTo(parent.bottom, 11.dp)
                    start.linkTo(parent.start, 1.dp)
                }, viewModel
        )
        Text(
            text = viewModel.song.value?.songName ?: "未知作品",
            fontSize = 12.sp,
            color = titleColor,
            modifier = Modifier.constrainAs(songName) {
                top.linkTo(songImage.top)
                start.linkTo(songImage.end, 7.dp)
                end.linkTo(songPlayType.start)
                width = Dimension.fillToConstraints
            },
            maxLines = 1
        )
        Text(text = viewModel.song.value?.artist ?: "未知艺术家",
            fontSize = 10.sp,
            color = colorResource(id = R.color.song_author),
            modifier = Modifier.constrainAs(songAuthor) {
                top.linkTo(songName.bottom, 2.dp)
                start.linkTo(songName.start)
                end.linkTo(songName.end)
                width = Dimension.fillToConstraints
            })
        ProgressBar(
            modifier = Modifier
                .height(2.dp)
                .constrainAs(songProgress) {
                    top.linkTo(songAuthor.bottom, 9.dp)
                    start.linkTo(songAuthor.start)
                    end.linkTo(songPlayStatus.start, 32.dp)
                    width = Dimension.fillToConstraints
                },
            progress = viewModel.state.mProgress.value,
            color = colorResource(id = R.color.song_author),
            cornerRadius = 1.dp,
            backgroundColor = Color.White
        )
        IconClick(
            onClick = {
                viewModel.dispatch(MainAction.PlayOrPauseSong)
            },
            res = if (viewModel.state.mPlayStatus.value != PLAY_STATUS.PLAYING) R.drawable.icon_song_detail_play else R.drawable.icon_song_detail_pause,
            modifier = Modifier.constrainAs(songPlayStatus) {
                top.linkTo(songPlayType.top)
                bottom.linkTo(songPlayType.bottom)
                end.linkTo(songPlayType.start, 10.dp)
            },
            iconSize = 30.dp
        )
        IconClick(
            onClick = {
                viewModel.dispatch(MainAction.ShowMusicDialog(BottomDialogEnum.PLAY_LIST_DIALOG))
            },
            res = R.drawable.icon_play_list,
            modifier = Modifier.constrainAs(songPlayType) {
                top.linkTo(songImage.top)
                bottom.linkTo(songImage.bottom)
                end.linkTo(parent.end, 14.dp)
            },
            iconSize = 24.dp
        )
    }
    LaunchedEffect(key1 = viewModel.state.currentPlayIndex.value) {
        viewModel.dispatch(MainAction.GetCurrentSong)
    }
}