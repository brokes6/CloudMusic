package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.InputMode.Companion.Keyboard
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.theme.*
import com.brookes6.cloudmusic.vm.LoginViewModel

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */

@Preview(showSystemUi = true)
@Composable
fun PhoneLoginPage(onNavController: (String) -> Unit = {}) {
    val viewModel: LoginViewModel = viewModel()
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Box(modifier = Modifier.background(mainBackground)) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp, 62.dp, 20.dp, 10.dp)
                .fillMaxSize()
                .background(secondaryBackground, RoundedCornerShape(39.dp))
                .padding(20.dp, 0.dp, 20.dp, 0.dp)
        ) {
            val (title, secondaryTitle, edit, login) = remember { createRefs() }
            Text(text = "Let’s Gooooo!", fontSize = 28.sp, color = titleColor,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 47.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                })
            Text(
                text = "One small step to reach the huge music store",
                color = colorResource(id = R.color.white_70),
                fontSize = 14.sp,
                maxLines = 2,
                modifier = Modifier
                    .width(275.dp)
                    .constrainAs(secondaryTitle) {
                        top.linkTo(title.bottom, 10.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end, 20.dp)
                        width = Dimension.fillToConstraints
                    }
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(titleColor, RoundedCornerShape(20.dp))
                    .constrainAs(edit) {
                        top.linkTo(secondaryTitle.bottom, 46.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                BasicTextField(
                    value = phone,
                    onValueChange = {
                        if (it.length <= 11) phone = it
                    },
                    textStyle = TextStyle(fontSize = 15.sp, color = normalText),
                    modifier = Modifier
                        .padding(20.dp, 24.dp, 20.dp, 24.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (phone.isEmpty()) {
                            Text(text = "请输入手机号", fontSize = 15.sp, color = hintText)
                        } else {
                            innerTextField()
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    maxLines = 1
                )
                Divider(startIndent = 8.dp, color = normalDivider)
                BasicTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    textStyle = TextStyle(fontSize = 15.sp, color = normalText),
                    modifier = Modifier
                        .padding(20.dp, 24.dp, 20.dp, 24.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) {
                                if (viewModel.state.isError.value) {
                                    Text(
                                        text = viewModel.state.message.value,
                                        fontSize = 15.sp,
                                        color = Color.Red
                                    )
                                    return@BasicTextField
                                }
                                if (password.isEmpty()) {
                                    Text(text = "请输入密码", fontSize = 15.sp, color = hintText)
                                } else {
                                    innerTextField()
                                }
                            }
                            Text(
                                text = "忘记密码",
                                fontSize = 12.sp,
                                color = colorResource(id = R.color.forget_password)
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    maxLines = 1
                )
            }
            LoginButton(
                "登陆",
                modifier = Modifier
                    .height(70.dp)
                    .constrainAs(login) {
                        bottom.linkTo(parent.bottom, 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    },
            ) {
                viewModel.dispatch(
                    LoginViewModel.LoginAction.PhoneLogin(
                        phone,
                        password,
                        onNavController
                    )
                )
            }
        }
    }
}