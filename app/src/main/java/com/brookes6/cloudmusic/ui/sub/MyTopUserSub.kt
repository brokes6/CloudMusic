package com.brookes6.cloudmusic.ui.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.ui.theme.secondaryBackground80Percent
import com.brookes6.cloudmusic.vm.MyViewModel

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */
@Composable
fun MyTopUserSub(
    viewModel: MyViewModel,
    onNavController: (String) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .paddingTop(62.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp, 37.dp, 20.dp, 0.dp)
                .fillMaxWidth()
                .background(secondaryBackground80Percent, RoundedCornerShape(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = viewModel.user.value?.profile?.nickname ?: "未知名称",
                modifier = Modifier.paddingTop(50.dp), fontSize = 20.sp, color = Color.White
            )
            Row(
                modifier = Modifier
                    .padding(0.dp, 8.dp, 0.dp, 8.dp)
            ) {
                Text(
                    text = "${viewModel.user.value?.profile?.follows} 关注",
                    modifier = Modifier,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${viewModel.user.value?.profile?.followeds} 粉丝",
                    modifier = Modifier.padding(10.dp, 0.dp, 10.dp, 0.dp),
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = "Lv.${viewModel.user.value?.level}",
                    modifier = Modifier,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .crossfade(true)
                .data(viewModel.user.value?.profile?.avatarUrl)
                .transformations(CircleCropTransformation())
                .build(),
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(75.dp)
                .align(Alignment.TopCenter)
        )
    }
}