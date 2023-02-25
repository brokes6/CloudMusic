package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.vm.MainViewModel

/**
 * Author: fuxinbo

 * Date: 2023/2/24

 * Description:
 */

@Composable
fun SongDetailPage(viewModel: MainViewModel = viewModel()) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.song_detail))
            .blur(15.dp)
    ) {
        val (cover, name, author, lyrics, function, progress) = createRefs()
        AsyncImage(
            model = "",
            contentDescription = stringResource(id = R.string.description),
            modifier = Modifier
                .size(231.dp)
                .constrainAs(cover) {
                    top.linkTo(parent.top, 41.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}