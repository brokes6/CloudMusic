package com.brookes6.cloudmusic.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.bean.TabModel

/**
 * Author: fuxinbo

 * Date: 2023/1/4

 * Description: Button扩展函数
 */

/**
 * 带文字的按钮
 *
 * @param text 文本内容
 * @param onClick 点击事件回调
 * @param modifier 按钮属性
 * @param color 文本颜色
 * @param fontSize 文本大小
 * @receiver
 */
@Composable
fun ButtonText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    buttonColor: ButtonColors = ButtonDefaults.buttonColors(),
    buttonShape: Shape = MaterialTheme.shapes.small,
    textColor: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
) {
    Button(onClick = onClick, shape = buttonShape, modifier = modifier, colors = buttonColor) {
        Text(text = text, fontSize = fontSize, color = textColor)
    }
}

/**
 * 底部导航栏
 *
 * @param tabList 导航栏展示的Tab数据
 * @param modifier 导航栏的样式
 * @param onItemClick item点击的回调
 * @receiver
 */
@Composable
fun BottomTab(
    tabList: MutableList<TabModel>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    var mSelectItemIndex by remember { mutableStateOf(0) }
    Row(modifier = modifier) {
        tabList.forEachIndexed { index, tab ->
            Column(modifier = Modifier
                .weight(1f)
                .clickable {
                    mSelectItemIndex = index
                    onItemClick.invoke(index)
                },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(id = if (mSelectItemIndex == index) tab.iconChecked else tab.iconNormal),
                    contentDescription = tab.title,
                    modifier = Modifier
                        .size(24.dp)
                        .paddingTop(4.dp)
                )
                Text(
                    text = tab.title,
                    fontSize = 10.sp,
                    color = if (mSelectItemIndex == index) Color.Black else Color.LightGray,
                    modifier = Modifier
                        .paddingTop(4.dp)
                        .paddingBottom(4.dp)
                )
            }
        }
    }
}