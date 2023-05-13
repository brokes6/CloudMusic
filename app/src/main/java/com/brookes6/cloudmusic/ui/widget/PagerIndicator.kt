package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.lt.compose_views.pager_indicator.PagerIndicator
import kotlinx.coroutines.flow.Flow

/**
 * Author: Sakura

 * Date: 2023/5/12

 * Description:
 */
@Composable
fun PagerIndicator(
    texts: List<String>,
    offsetPercentWithSelectFlow: Flow<Float>,
    selectIndexFlow: Flow<Int>,
    onIndicatorClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
    margin: Dp = 10.dp,
    userCanScroll: Boolean = true,
) {
    PagerIndicator(
        size = texts.size,
        offsetPercentWithSelectFlow = offsetPercentWithSelectFlow,
        selectIndexFlow = selectIndexFlow,
        indicatorItem = { index ->
            val selectIndex by selectIndexFlow.collectAsState(0)
            Box(
                modifier = Modifier
                    .size(68.dp, 34.dp)
                    .clickable {
                        if (index != selectIndex)
                            onIndicatorClick(index)
                    },
            ) {
                Text(
                    text = texts[index],
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        },
        selectIndicatorItem = {
            val selectIndex by selectIndexFlow.collectAsState(0)
            Row(
                modifier = Modifier
                    .background(colorResource(id = R.color.song_author), RoundedCornerShape(8.dp))
                    .height(34.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.icon_search_type),
                    contentDescription = stringResource(id = R.string.description),
                    modifier = Modifier
                        .padding(8.dp, 5.dp, 8.dp, 5.dp)
                        .size(24.dp),
                    tint = Color.Unspecified,
                )
                Text(
                    text = texts[selectIndex],
                    fontSize = 14.sp,
                    color = Color.White,
                    modifier = Modifier.paddingEnd(12.dp),
                )
            }
        },
        modifier = modifier,
        margin = margin,
        orientation = Orientation.Horizontal,
        userCanScroll = userCanScroll,
    )
}

/**
 * 根据this的百分比(0-1或1-0)来计算start到end对应的值,并返回
 */
internal fun Float/*percentage*/.getPercentageValue(startValue: Float, endValue: Float): Float =
    if (startValue == endValue) startValue
    else (endValue - startValue) * this + startValue

internal fun Float/*percentage*/.getPercentageValue(startValue: Color, endValue: Color): Color =
    Color(
        alpha = getPercentageValue(startValue.alpha, endValue.alpha),
        red = getPercentageValue(startValue.red, endValue.red),
        green = getPercentageValue(startValue.green, endValue.green),
        blue = getPercentageValue(startValue.blue, endValue.blue),
    )