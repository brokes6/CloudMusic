package com.brookes6.cloudmusic.ui.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.TokenViewModel
import com.brookes6.net.api.Api
import com.brookes6.repository.model.UserModel
import com.drake.net.Post
import com.drake.net.utils.scopeNet
import com.drake.serialize.serialize.serialize

/**
 * @Author fuxinbo
 * @Date 2023/1/16 10:32
 * @Description TODO
 */
@Preview(showSystemUi = true)
@Composable
fun SplashPage(
    navController: NavController? = null,
    mTokenVM: TokenViewModel? = null,
    viewModel: MainViewModel? = null
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_splash_bg), null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    LaunchedEffect(true) {
        scopeNet {
            Post<UserModel>(Api.LOGIN_STATUS) {
                param("timestamp", System.currentTimeMillis())
            }.await().also {
                LogUtils.d("当前帐号状态为:${it}")
                if (it.account?.status == 0) {
                    LogUtils.d("账号ID为 -> ${it.account?.id}")
                    // 登录状态为已登录
                    serialize(AppConstant.IS_LOGIN to true)
                    serialize(AppConstant.USER_ID to it.account?.id)
                    viewModel?.state?.let { state ->
                        state.isLogin.value = true
                        state.isShowBottomTab.value = true
                    }
                    navController?.navigate(RouteConstant.HOME) {
                        popUpTo(RouteConstant.SPLASH_PAGE) { inclusive = true }
                    }
                } else {
                    // 登录状态为未登录
                    serialize(AppConstant.IS_LOGIN to false)
                    navController?.navigate(RouteConstant.LOGIN) {
                        popUpTo(RouteConstant.SPLASH_PAGE) { inclusive = true }
                    }
                }
            }
        }
    }
}