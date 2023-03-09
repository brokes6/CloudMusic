package com.brookes6.cloudmusic.ui.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingBottom
import com.brookes6.cloudmusic.extensions.paddingTop
import com.brookes6.cloudmusic.ui.theme.secondaryBackground80Percent
import com.brookes6.cloudmusic.ui.widget.ItemFunction
import com.brookes6.cloudmusic.vm.MyViewModel

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */
@Composable
fun MyFunctionSub(
    viewModel: MyViewModel,
    onNavController: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(20.dp, 25.dp, 20.dp, 0.dp)
            .fillMaxWidth()
            .background(secondaryBackground80Percent, RoundedCornerShape(20.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .paddingTop(30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ItemFunction(R.drawable.ic_my_recently_played, "最近播放") {

            }
            ItemFunction(res = R.drawable.ic_my_download, title = "本地下载") {

            }
            ItemFunction(res = R.drawable.ic_my_cloud, title = "云盘") {

            }
            ItemFunction(res = R.drawable.ic_my_buying, title = "已购") {

            }
        }
        Row(
            modifier = Modifier
                .paddingBottom(30.dp)
                .fillMaxWidth()
                .paddingTop(30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ItemFunction(res = R.drawable.ic_my_friend, title = "我的好友") {

            }
            ItemFunction(res = R.drawable.ic_my_like, title = "收藏和赞") {

            }
            ItemFunction(res = R.drawable.ic_my_radio_station, title = "我的电台") {

            }
            ItemFunction(res = R.drawable.ic_my_music_jar, title = "音乐罐子") {

            }
        }
    }
}