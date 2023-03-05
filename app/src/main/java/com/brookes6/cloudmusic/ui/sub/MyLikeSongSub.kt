package com.brookes6.cloudmusic.ui.sub

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.brookes6.cloudmusic.ui.theme.secondaryBackground80Percent
import com.brookes6.cloudmusic.vm.MyViewModel

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */

@Composable
fun MyLikeSongSub(viewModel: MyViewModel, navController: NavController? = null) {
    ConstraintLayout(
        modifier = Modifier
            .padding(20.dp, 20.dp, 20.dp, 0.dp)
            .fillMaxWidth()
            .background(secondaryBackground80Percent, RoundedCornerShape(20.dp))
    ) {
        val (cover, name, info, playType) = createRefs()
//        AsyncImage(model = ImageRequest.Builder(LocalContext.current)
//            .crossfade(true)
//            .data(viewModel.), contentDescription = )

    }
}