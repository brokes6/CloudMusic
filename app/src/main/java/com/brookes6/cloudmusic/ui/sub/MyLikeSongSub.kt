package com.brookes6.cloudmusic.ui.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.navigateAndArgument
import com.brookes6.cloudmusic.extensions.paddingStart
import com.brookes6.cloudmusic.ui.theme.secondaryBackground80Percent
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.MyViewModel

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */

@Composable
fun MyLikeSongSub(
    viewModel: MyViewModel,
    mainVM: MainViewModel,
    onNavController: NavController
) {
    ConstraintLayout(
        modifier = Modifier
            .clickable(interactionSource = remember {
                MutableInteractionSource()
            }, indication = null) {
                mainVM.state.isShowBottomTab.value = false
                onNavController.navigateAndArgument(
                    RouteConstant.SONG_PLAY_LIST,
                    AppConstant.PLAY_LIST_INFO to viewModel.userLikePlayList.value
                )
            }
            .padding(20.dp, 20.dp, 20.dp, 0.dp)
            .fillMaxWidth()
            .background(secondaryBackground80Percent, RoundedCornerShape(20.dp))
    ) {
        val (cover, like, name, info, playType) = createRefs()
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(viewModel.userLikePlayList.value?.coverImgUrl)
                .transformations(RoundedCornersTransformation(20f))
                .build(),
            contentDescription = stringResource(
                id = R.string.description
            ),
            modifier = Modifier
                .size(50.dp)
                .constrainAs(cover) {
                    top.linkTo(parent.top, 15.dp)
                    start.linkTo(parent.start, 15.dp)
                    bottom.linkTo(parent.bottom, 15.dp)
                }
        )
        Icon(
            bitmap = ImageBitmap.imageResource(id = R.drawable.icon_my_like_playlist),
            contentDescription = stringResource(id = R.string.description),
            tint = Color.Unspecified,
            modifier = Modifier.constrainAs(like) {
                start.linkTo(cover.start)
                top.linkTo(cover.top)
                end.linkTo(cover.end)
                bottom.linkTo(cover.bottom)
            }
        )
        Text(
            text = viewModel.userLikePlayList.value?.name ?: "",
            fontSize = 12.sp,
            color = Color.White,
            modifier = Modifier.constrainAs(name) {
                top.linkTo(cover.top, 5.dp)
                start.linkTo(cover.end, 10.dp)
            }
        )
        Text(
            text = viewModel.userLikePlayList.value?.trackCount.toString() + "首",
            fontSize = 10.sp,
            color = Color.Gray,
            modifier = Modifier.constrainAs(info) {
                top.linkTo(name.bottom, 5.dp)
                start.linkTo(name.start)
            }
        )
        Row(
            modifier = Modifier
                .border(1.dp, Color.Gray, RoundedCornerShape(39.dp))
                .padding(10.dp, 5.dp, 10.dp, 5.dp)
                .constrainAs(playType) {
                    end.linkTo(parent.end, 15.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                bitmap = ImageBitmap.imageResource(id = R.drawable.icon_my_like_heart_type),
                contentDescription = stringResource(id = R.string.description),
                tint = Color.Unspecified,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "心动模式",
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.paddingStart(5.dp)
            )
        }
    }
}