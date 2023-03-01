package com.brookes6.cloudmusic.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brookes6.cloudmusic.R

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description: 可点击的Icon封装
 */

@Composable
fun IconClick(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    @DrawableRes res: Int,
    iconSize: Dp = 24.dp
) {
    IconButton(
        onClick = {
            onClick.invoke()
        }, modifier = modifier
            .size(iconSize)
    ) {
        Icon(
            bitmap = ImageBitmap.imageResource(id = res),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier.fillMaxSize(),
            tint = Color.Unspecified,
        )
    }
}