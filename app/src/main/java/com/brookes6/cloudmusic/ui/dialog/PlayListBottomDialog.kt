package com.brookes6.cloudmusic.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.bean.type.BottomDialogEnum
import com.brookes6.cloudmusic.extensions.paddingEnd
import com.brookes6.cloudmusic.manager.MusicManager
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.IconClick
import com.brookes6.cloudmusic.ui.widget.SongItem
import com.brookes6.cloudmusic.vm.MainViewModel

/**
 * Author: Sakura

 * Date: 2023/5/10

 * Description: 播放列表弹窗
 */
@Composable
fun PlayListBottomDialog(mainVM: MainViewModel) {
    Column(
        modifier = Modifier
            .padding(16.dp, 0.dp, 16.dp, 16.dp)
            .height(350.dp)
            .background(secondaryBackground, RoundedCornerShape(20.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "当前播放",
                fontSize = 16.sp,
                color = Color.White,
            )
            Text(
                text = "(${MusicManager.instance.getPlayList().size})",
                fontSize = 10.sp,
                color = Color.Gray,
                modifier = Modifier.weight(1f)
            )
            IconClick(
                onClick = {
                    mainVM.dispatch(MainAction.ShowMusicDialog(BottomDialogEnum.NORMAL))
                },
                res = R.drawable.icon_dialog_close,
                modifier = Modifier.paddingEnd(16.dp),
                iconSize = 12.dp
            )
        }
        LazyColumn(
            modifier = Modifier
                .padding(0.dp, 10.dp, 0.dp, 16.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {
            itemsIndexed(MusicManager.instance.getPlayList(), key = { _, item ->
                item.id
            }) { index, item ->
                SongItem(
                    index = index,
                    item = item,
                    isCurrent = MusicManager.instance.getPlayPosition() == index
                ) { position ->
                    mainVM.dispatch(MainAction.PlaySong(index, MusicManager.instance.getPlayList()))
                    mainVM.dispatch(MainAction.ShowMusicDialog(BottomDialogEnum.NORMAL))
                }
            }
        }
    }
}