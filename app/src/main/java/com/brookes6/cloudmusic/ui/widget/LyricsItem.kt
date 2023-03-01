package com.brookes6.cloudmusic.ui.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.repository.model.Lyrics

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description:
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.LyricsItem(
    lyrics: Lyrics?,
    current: Boolean = false,
    currentTextElementHeightPxState: MutableState<Int>,
    textSize: Int,
    textColor: Color = colorResource(id = R.color.white),
    centerAlign: Boolean = false,
    showSubText: Boolean = true,
    onClick: () -> Unit
) {
    // 当前歌词，若不显示翻译则只显示主句
    val mainLyrics = if (showSubText) lyrics?.lyric ?: "" else lyrics?.lyricSub ?: ""
    // 当前正在播放的歌词高亮
    val textAlpha = animateFloatAsState(if (current) 1F else 0.32F).value
    // 歌词文本对齐方式，可选左 / 中
    val align = if (centerAlign) TextAlign.Center else TextAlign.Left
    Card(
        modifier = Modifier
            .animateItemPlacement()
            .fillMaxWidth()
            .onSizeChanged {
                if (current) {
                    // 告知当前高亮歌词 Item 高度
                    currentTextElementHeightPxState.value = it.height
                }
            }
            .padding(0.dp, (textSize * 0.1F).dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        val paddingY = (textSize * 0.3F).dp
        // 这里使用 Column 是为了若以后拓展具体显示
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(8.dp, paddingY),
            verticalArrangement = Arrangement.Center
        ) {
            val mainTextSize = textSize.sp
            Text(
                modifier = Modifier
                    .alpha(textAlpha)
                    .fillMaxWidth(),
                text = mainLyrics,
                fontSize = mainTextSize,
                color = textColor,
                textAlign = align
            )
        }
    }
}