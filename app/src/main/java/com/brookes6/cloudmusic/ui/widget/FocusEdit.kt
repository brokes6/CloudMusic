package com.brookes6.cloudmusic.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brookes6.cloudmusic.ui.theme.normalText

/**
 * Author: fuxinbo

 * Date: 2023/2/17

 * Description: 自动切换焦点输入框
 */
@Preview
@Composable
fun FocusEdit(modifier: Modifier = Modifier, onPhoneCodeCallback: (code: String) -> Unit = {}) {
    var focusStatus by remember { mutableStateOf(1) }
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }

    var code1 by remember { mutableStateOf("") }
    var code2 by remember { mutableStateOf("") }
    var code3 by remember { mutableStateOf("") }
    var code4 by remember { mutableStateOf("") }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TextField(
            value = code1,
            onValueChange = {
                when (it.length) {
                    1 -> {
                        code1 = it
                        focusStatus = 2
                        focusRequester2.requestFocus()
                    }
                    else -> {
                        code1 = it
                        focusStatus = 1
                    }
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester1)
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = normalText,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            value = code2,
            onValueChange = {
                when (it.length) {
                    1 -> {
                        code2 = it
                        focusStatus = 3
                        focusRequester3.requestFocus()
                    }
                    else -> {
                        code2 = it
                        focusStatus = 1
                        focusRequester1.requestFocus()
                    }
                }
            },
            modifier = Modifier
                .focusRequester(focusRequester2)
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = normalText,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            value = code3, onValueChange = {
                when (it.length) {
                    1 -> {
                        code3 = it
                        focusStatus = 4
                        focusRequester4.requestFocus()
                    }
                    else -> {
                        code3 = it
                        focusStatus = 2
                        focusRequester2.requestFocus()
                    }
                }
            }, modifier = Modifier
                .focusRequester(focusRequester3)
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = normalText,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        TextField(
            value = code4, onValueChange = {
                when (it.length) {
                    1 -> {
                        code4 = it
                        focusStatus = 0
                        onPhoneCodeCallback.invoke(code1 + code2 + code3 + code4)
                    }
                    else -> {
                        code4 = it
                        focusStatus = 3
                        focusRequester3.requestFocus()
                    }
                }
            }, modifier = Modifier
                .focusRequester(focusRequester4)
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White, RoundedCornerShape(20.dp)),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = TextStyle(
                fontSize = 25.sp,
                color = normalText,
                textAlign = TextAlign.Center
            ),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}