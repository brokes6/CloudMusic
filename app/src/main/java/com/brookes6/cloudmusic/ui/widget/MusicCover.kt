package com.brookes6.cloudmusic.ui.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.vm.MainViewModel

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description:音乐旋转封面
 */

@Preview
@Composable
fun MusicCover(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel()) {
    // 管理无限循环动画
    val infiniteTransition = rememberInfiniteTransition()
    // 创建一个float类型的子动画
    val angle by infiniteTransition.animateFloat(
        initialValue = 0F, //动画创建后，会从[initialValue] 执行至[targetValue]，
        targetValue = 360F,
        animationSpec = infiniteRepeatable(
            //tween是补间动画，使用线性[LinearEasing]曲线无限重复1000 ms的补间动画
            animation = tween(15 * 1000, easing = LinearEasing),
            //每次迭代后的动画(即每1000毫秒), 动画将从上面定义的[initialValue]重新开始执行
            //动画执行模式
            // repeatMode = RepeatMode.Restart //此处不需要，它自己会无线重复执行
        )
    )
    Image(
        painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(viewModel.song.value?.songCover)
                .size(43)
                .build()
        ),
        stringResource(id = R.string.description),
        modifier = modifier
            .size(43.dp)
            .clip(CircleShape)
            .graphicsLayer {
                if (viewModel.state.mPlayStatus.value != PLAY_STATUS.PLAYING) {
                    rotationZ = 0F
                    return@graphicsLayer
                }
                rotationZ = angle
            }
    )
}