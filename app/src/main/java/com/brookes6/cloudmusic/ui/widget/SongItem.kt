package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.theme.titleColor
import com.bumptech.glide.request.RequestOptions
import com.lzx.starrysky.SongInfo
import com.skydoves.landscapist.glide.GlideImage


@Preview
@Composable
fun SongItem(modifier: Modifier = Modifier, item: SongInfo? = null) {
    ConstraintLayout(modifier = modifier) {
        val (img, info) = createRefs()
        GlideImage(
            imageModel = { item?.songCover },
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(20.dp))
                .constrainAs(img) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            requestOptions = { RequestOptions().override(136).centerCrop() }
        )
        Column(
            modifier = Modifier
                .background(colorResource(id = R.color.song_info), RoundedCornerShape(9.dp))
                .constrainAs(info) {
                    start.linkTo(parent.start, 10.dp)
                    end.linkTo(parent.end, 10.dp)
                    bottom.linkTo(parent.bottom, 10.dp)
                    width = Dimension.fillToConstraints
                }
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = item?.songName ?: "", fontSize = 12.sp, color = titleColor,
                modifier = Modifier
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .fillMaxWidth(),
                maxLines = 1
            )
            Text(
                text = item?.artist ?: "",
                fontSize = 10.sp,
                color = colorResource(id = R.color.song_author),
                modifier = Modifier
                    .padding(10.dp, 1.dp, 0.dp, 8.dp)
                    .fillMaxWidth(),
                maxLines = 1
            )
        }
    }
}