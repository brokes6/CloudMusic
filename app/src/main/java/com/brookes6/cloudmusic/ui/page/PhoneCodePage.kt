package com.brookes6.cloudmusic.ui.page

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brookes6.cloudmusic.App
import com.brookes6.cloudmusic.ui.theme.*
import com.brookes6.cloudmusic.ui.widget.FocusEdit
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.LoginViewModel

/**
 * Author: fuxinbo

 * Date: 2023/2/17

 * Description:
 */
@Composable
fun PhoneCodePage(onNavController: (String) -> Unit = {}) {
    val viewModel: LoginViewModel = viewModel()
    var phone by remember { mutableStateOf("") }
    var mPhoneCode = ""
    Box(Modifier.background(mainBackground)) {
        ConstraintLayout(
            modifier = Modifier
                .padding(20.dp, 62.dp, 20.dp)
                .background(
                    secondaryBackground,
                    RoundedCornerShape(40.dp)
                )
                .fillMaxWidth()
        ) {
            val (title, hint, phoneNum, phoneCode, phoneCountdown, button) = remember { createRefs() }
            Text(
                text = "验证码登录",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 47.dp)
                    start.linkTo(parent.start, 20.dp)
                })
            Text(
                text = "请在下方输入您的手机号来获取验证码并登录",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(hint) {
                    top.linkTo(title.bottom, 13.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end, 40.dp)
                    width = Dimension.fillToConstraints
                })
            AnimatedVisibility(
                visible = !viewModel.state.isShowPhoneCode.value,
                modifier = Modifier.constrainAs(phoneNum) {
                    start.linkTo(parent.start, 20.dp)
                    top.linkTo(hint.bottom, 46.dp)
                    end.linkTo(parent.end, 20.dp)
                    width = Dimension.fillToConstraints
                },
            ) {
                BasicTextField(value = phone, onValueChange = {
                    if (it.length <= 11) phone = it
                },
                    textStyle = TextStyle(fontSize = 15.sp, color = normalText),
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .padding(20.dp, 24.dp, 20.dp, 24.dp)
                        .fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        if (phone.isEmpty()) {
                            Text(text = "请输入手机号", fontSize = 15.sp, color = hintText)
                        } else {
                            innerTextField()
                        }
                    }
                )
            }

            AnimatedVisibility(
                visible = viewModel.state.isShowPhoneCode.value,
                modifier = Modifier.constrainAs(phoneCode) {
                    start.linkTo(parent.start, 20.dp)
                    top.linkTo(hint.bottom, 46.dp)
                    end.linkTo(parent.end, 20.dp)
                    width = Dimension.fillToConstraints
                }) {
                FocusEdit() {
                    LogUtils.d("当前输入的验证码为:${it}")
                    mPhoneCode = it
                }
            }

            LoginButton(
                if (!viewModel.state.isShowPhoneCode.value) "发送验证码" else "登录",
                modifier = Modifier
                    .height(70.dp)
                    .constrainAs(button) {
                        top.linkTo(hint.bottom, 208.dp)
                        start.linkTo(parent.start, 20.dp)
                        bottom.linkTo(parent.bottom, 24.dp)
                        end.linkTo(parent.end, 20.dp)
                        width = Dimension.fillToConstraints
                    },
                colors = if (!viewModel.state.isShowPhoneCode.value) ButtonDefaults.buttonColors(
                    secondaryBackground_2
                ) else ButtonDefaults.buttonColors(mainButtonColor)
            ) {
                viewModel.dispatch(
                    if (!viewModel.state.isShowPhoneCode.value) {
                        if (phone.isEmpty()) {
                            Toast.makeText(App.content, "手机号不可为空", Toast.LENGTH_SHORT).show()
                            return@LoginButton
                        }
                        LoginViewModel.LoginAction.SendPhoneCode(phone)
                    } else {
                        if (mPhoneCode.isEmpty()) {
                            Toast.makeText(App.content, "请输入验证码", Toast.LENGTH_SHORT).show()
                            return@LoginButton
                        }
                        LoginViewModel.LoginAction.PhoneLogin(
                            phone,
                            "",
                            onNavController,
                            mPhoneCode
                        )
                    }

                )
            }
        }
    }
}