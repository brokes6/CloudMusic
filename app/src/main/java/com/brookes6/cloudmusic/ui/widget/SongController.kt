package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.brookes6.cloudmusic.vm.MainViewModel

/**
 * @Author fuxinbo
 * @Date 2023/1/15 15:53
 * @Description TODO
 */
@Preview
@Composable
fun SongController(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    ConstraintLayout(
        modifier = modifier
            .padding(20.dp, 0.dp, 20.dp, 0.dp)
            .fillMaxWidth()
            .background(
                colorResource(id = R.color.song_controller),
                RoundedCornerShape(20.dp)
            )
            .padding(4.dp)
    ) {
        val (songImage, songName, songAuthor, songProgress, songPlayType, songPlayStatus) = createRefs()
        Image(bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_song_test),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(43.dp)
                .clip(CircleShape)
                .constrainAs(songImage) {
                    top.linkTo(parent.top, 4.dp)
                    bottom.linkTo(parent.bottom, 11.dp)
                    start.linkTo(parent.start, 1.dp)
                })
        Text(
            text = "Someone You Live", fontSize = 12.sp, color = titleColor,
            modifier = Modifier.constrainAs(songName) {
                top.linkTo(songImage.top)
                start.linkTo(songImage.end, 7.dp)
                end.linkTo(songPlayType.start)
                width = Dimension.fillToConstraints
            },
            maxLines = 1
        )
        Text(text = "Arian Grande",
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
                    end.linkTo(songPlayType.start, 32.dp)
                    width = Dimension.fillToConstraints
                },
            progress = 0.70f,
            color = colorResource(id = R.color.song_author),
            cornerRadius = 1.dp,
            backgroundColor = Color.White
        )
        Icon(
            bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_song_type_random),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier.constrainAs(songPlayType) {
                top.linkTo(songPlayStatus.top)
                bottom.linkTo(songPlayStatus.bottom)
                end.linkTo(songPlayStatus.start, 10.dp)
            },
            tint = Color.Unspecified
        )
        Box(modifier = Modifier
            .size(36.dp)
            .clickable {
                viewModel.state.isShowSongController.value = false
            }
            .background(colorResource(id = R.color.song_author), CircleShape)
            .constrainAs(songPlayStatus) {
                top.linkTo(songImage.top)
                bottom.linkTo(songImage.bottom)
                end.linkTo(parent.end, 14.dp)
            },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_song_pause),
                contentDescription = "播放",
                modifier = Modifier.size(24.dp),
                tint = Color.Unspecified
            )
        }
    }
}