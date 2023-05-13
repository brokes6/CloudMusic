package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.repository.model.SearchHistoryModel

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
@Composable
fun SearchHistoryItem(data: SearchHistoryModel, clickCallback: (String) -> Unit) {
    Row(
        modifier = Modifier
            .paddingEnd(13.dp)
            .background(
                colorResource(id = R.color.song_author),
                RoundedCornerShape(8.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = data.searchText, modifier = Modifier
                .padding(10.dp, 9.dp, 14.dp, 9.dp),
            color = Color.White,
            fontSize = 14.sp
        )
        IconClick(onClick = {
            clickCallback.invoke(data.searchText)
        }, modifier = Modifier.paddingEnd(10.dp), res = R.drawable.icon_down_arrow)
    }
}