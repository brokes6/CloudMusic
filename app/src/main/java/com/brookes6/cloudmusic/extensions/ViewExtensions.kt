package com.brookes6.cloudmusic.extensions

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Author: fuxinbo

 * Date: 2023/1/5

 * Description:
 */

fun Modifier.paddingTop(value: Dp): Modifier {
    return this.padding(0.dp, value, 0.dp, 0.dp)
}

fun Modifier.paddingBottom(value: Dp): Modifier {
    return this.padding(0.dp, 0.dp, 0.dp, value)
}

fun Modifier.paddingStart(value: Dp): Modifier {
    return this.padding(value, 0.dp, 0.dp, 0.dp)
}

fun Modifier.paddingEnd(value: Dp): Modifier {
    return this.padding(0.dp, 0.dp, value, 0.dp)
}