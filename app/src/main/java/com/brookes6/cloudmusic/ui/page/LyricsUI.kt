package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.App
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.view.FocusLayoutManager.Companion.dp2px
import com.brookes6.cloudmusic.ui.widget.LyricsItem
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.repository.model.Lyrics

/**
 * Author: fuxinbo

 * Date: 2023/3/1

 * Description:
 */
@Composable
fun LyricsUI(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    liveTime: Long = 0L,
    lyricsEntryList: List<Lyrics?>,
    textColor: Color = colorResource(id = R.color.white),
    textSize: Int = 20,
    paddingWidth: Dp = 30.dp,
    alignCenter: Boolean = false,
    openTranslation: Boolean = true,
    itemOnClick: (Lyrics?) -> Unit,
) {
    val state = rememberLazyListState()
    // 当没歌词的时候
    if (lyricsEntryList.isEmpty()) {
        Box(
            modifier = modifier
        ) {
            Text(
                text = stringResource(id = R.string.no_lyrics),
                modifier = Modifier.align(Alignment.Center),
                color = textColor,
                fontSize = textSize.sp
            )
        }
    } else {
        val currentTextElementHeightPx = remember { mutableStateOf(0) }
        BoxWithConstraints(modifier = modifier) {
            // 前后空白
            val blackItem: (LazyListScope.() -> Unit) = {
                item { Box(modifier = Modifier.height(maxHeight / 2)) }
            }
            // 歌词主体
            val lyricsEntryListItems: (LazyListScope.() -> Unit) = {
                this.itemsIndexed(lyricsEntryList) { index, lyricsEntry ->
                    LyricsItem(
                        current = index == viewModel.state.mCurrentLyricIndex.value,
                        currentTextElementHeightPxState = currentTextElementHeightPx,
                        lyrics = lyricsEntry,
                        textColor = textColor,
                        textSize = textSize,
                        centerAlign = alignCenter,
                        showSubText = openTranslation,
                        onClick = {
                            itemOnClick(lyricsEntry)
                        }
                    )
                }
            }
            LazyColumn(Modifier
                .fillMaxSize()
                .padding(paddingWidth, 0.dp)
                .graphicsLayer { alpha = 0.99F }
                .drawWithContent {
                    val colors = listOf(
                        Color.Transparent, Color.Black, Color.Black, Color.Black, Color.Black,
                        Color.Black, Color.Black, Color.Black, Color.Transparent
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                },
                state = state
            ) {
                blackItem()
                lyricsEntryListItems()
                blackItem()
            }
            // 定位中间
            LaunchedEffect(key1 = liveTime, key2 = currentTextElementHeightPx.value, block = {
                val height = (dp2px(
                    App.content,
                    maxHeight.value
                ).toInt() - currentTextElementHeightPx.value) / 2
                val index = findShowLine(lyricsEntryList, liveTime)
                if (index < viewModel.state.mCurrentLyricIndex.value) return@LaunchedEffect
                viewModel.state.mCurrentLyricIndex.value = index
                state.animateScrollToItem((index + 1).coerceAtLeast(0), -height)
            })
            LaunchedEffect(key1 = viewModel.state.mResetLyric.value, block = {
                if (viewModel.state.isInitPage2.value) return@LaunchedEffect
                LogUtils.i("重置歌词进度", "Song")
                viewModel.state.mCurrentLyricIndex.value = 0
                viewModel.state.isInitPage2.value = true
            })
        }
    }
}

/**
 * 寻找到当前播放的歌词位置
 *
 * @param list
 * @param time
 * @return
 */
private fun findShowLine(list: List<Lyrics?>, time: Long): Int {
    var index = 0
    list.forEachIndexed { i, lyrics ->
        if (lyrics?.time == time) {
            index = i
            return@forEachIndexed
        }
    }
    return index
}