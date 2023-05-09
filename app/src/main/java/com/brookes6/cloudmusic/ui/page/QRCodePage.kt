package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.mainButtonColor
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.TokenViewModel

/**
 * Author: fuxinbo

 * Date: 2023/2/19

 * Description:
 */
@Preview
@Composable
fun QRCodePage(
    viewModel: LoginViewModel = viewModel(),
    tokenVM: TokenViewModel = viewModel(),
    onNavController: (String) -> Unit = {}
) {
    var mQRCodeSrc by remember { mutableStateOf(byteArrayOf()) }
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
            val (title, hint, qrCode, message, refresh, button) = remember { createRefs() }
            Text(
                text = "二维码登录",
                color = Color.White,
                fontSize = 28.sp,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(parent.top, 47.dp)
                    start.linkTo(parent.start, 20.dp)
                })
            Text(
                text = "使用网易云音乐APP扫描下方二维码进行预登录",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.constrainAs(hint) {
                    top.linkTo(title.bottom, 13.dp)
                    start.linkTo(title.start)
                    end.linkTo(parent.end, 40.dp)
                    width = Dimension.fillToConstraints
                })
            AsyncImage(
                model = mQRCodeSrc,
                stringResource(id = R.string.description),
                modifier = Modifier
                    .size(150.dp)
                    .constrainAs(qrCode) {
                        top.linkTo(hint.bottom, 20.dp)
                        start.linkTo(parent.start, 20.dp)
                        end.linkTo(parent.end, 20.dp)
                    })
            Text(
                text = viewModel.state.mQRCodeMessage.value,
                modifier = Modifier.constrainAs(message) {
                    top.linkTo(qrCode.bottom, 15.dp)
                    start.linkTo(qrCode.start)
                    end.linkTo(qrCode.end)
                }, fontSize = 14.sp, color = Color.White
            )
            LoginButton(
                "验证",
                modifier = Modifier
                    .height(70.dp)
                    .constrainAs(button) {
                        top.linkTo(hint.bottom, 208.dp)
                        start.linkTo(parent.start, 20.dp)
                        bottom.linkTo(parent.bottom, 24.dp)
                        end.linkTo(parent.end, 20.dp)
                        width = Dimension.fillToConstraints
                    },
                colors = ButtonDefaults.buttonColors(mainButtonColor)
            ) {
                viewModel.dispatch(LoginViewModel.LoginAction.JudgeQRCodeState(onNavController,tokenVM.token))
            }
        }
    }
    LaunchedEffect(key1 = true, block = {
        viewModel.dispatch(LoginViewModel.LoginAction.CreateQRCode {
            mQRCodeSrc = it
        })
    })
}