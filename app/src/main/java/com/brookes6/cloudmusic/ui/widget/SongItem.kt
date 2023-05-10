package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.lzx.starrysky.SongInfo


@Composable
fun SongItem(
    index: Int,
    item: SongInfo? = null,
    isCurrent: Boolean = false,
    onClickCallBack: (index: Int) -> Unit = {}
) {
    Row(
        modifier = Modifier
            .background(
                if (isCurrent) Color.Gray else secondaryBackground,
                if (isCurrent) RoundedCornerShape(10.dp) else RectangleShape
            )
            .padding(20.dp, 12.5.dp, 0.dp, 12.5.dp)
            .fillMaxWidth()
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                onClickCallBack.invoke(index)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "#${index + 1}", fontSize = 12.sp, color = Color.White,
            modifier = Modifier.width(35.dp)
        )
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(item?.songCover)
                .transformations(RoundedCornersTransformation(10f))
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .paddingStart(22.dp)
                .size(32.dp)
        )
        Column(
            modifier = Modifier
                .paddingStart(20.dp)
                .weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item?.songName ?: "未知歌曲",
                fontSize = 12.sp,
                color = Color.White
            )
            Text(
                text = item?.artist ?: "未知作者",
                fontSize = 10.sp,
                color = colorResource(id = R.color.song_author),
                modifier = Modifier.paddingTop(2.dp)
            )
        }
        IconClick(
            onClick = {

            }, res = R.drawable.icon_item_more,
            iconSize = 24.dp,
            modifier = Modifier.paddingEnd(15.dp)
        )
    }
}