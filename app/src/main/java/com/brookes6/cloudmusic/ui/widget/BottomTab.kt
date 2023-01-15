package com.brookes6.cloudmusic.ui.page

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.bean.BottomTabBean
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground

/**
 * @Author fuxinbo
 * @Date 2023/1/15 14:32
 * @Description 主页底部导航栏
 */
@Preview
@Composable
fun BottomTab(
    modifier: Modifier = Modifier,
    tabList: MutableList<BottomTabBean> = mutableListOf()
) {
    var mSelectItemIndex by remember { mutableStateOf(0) }
    Row(
        modifier = modifier
    ) {
        tabList.forEachIndexed { index, bean ->
            Row(
                modifier = Modifier
                    .weight(if (mSelectItemIndex == index) 2f else 1f)
                    .background(
                        if (mSelectItemIndex == index) colorResource(id = R.color.bottomTabSelect) else secondaryBackground,
                        RoundedCornerShape(16.dp)
                    )
                    .clickable {
                        mSelectItemIndex = index
                    }
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    bitmap = ImageBitmap.imageResource(id = if (mSelectItemIndex == index) bean.selectIcon else bean.normalIcon),
                    contentDescription = stringResource(id = R.string.description),
                    modifier = Modifier
                        .padding(0.dp, 14.dp, 18.dp, 14.dp)
                        .size(24.dp),
                    tint = Color.Unspecified
                )
                if (mSelectItemIndex == index) Text(
                    text = bean.title,
                    fontSize = 12.sp,
                    color = mainBackground
                )
            }
        }
    }
}