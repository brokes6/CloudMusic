package com.brookes6.cloudmusic.ui.widget

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingTop

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */

@Composable
fun ItemFunction(
    @DrawableRes res: Int,
    title: String,
    iconSize: Dp = 30.dp,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(iconSize + 20.dp)
            .clickable {
                onClick.invoke()
            }
    ) {
        Icon(
            bitmap = ImageBitmap.imageResource(id = res),
            contentDescription = stringResource(id = R.string.description),
            tint = Color.Unspecified,
            modifier = Modifier.size(iconSize)
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.paddingTop(5.dp)
        )
    }
}