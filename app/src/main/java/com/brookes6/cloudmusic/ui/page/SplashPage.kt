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
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.manager.DataBaseManager
import com.brookes6.cloudmusic.state.PAGE_TYPE
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.drake.net.Post
import com.drake.net.utils.scopeNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @Author fuxinbo
 * @Date 2023/1/16 10:32
 * @Description TODO
 */
@Preview(showSystemUi = true)
@Composable
fun SplashPage(navController: NavController? = null, viewModel: MainViewModel? = null) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            bitmap = ImageBitmap.imageResource(id = R.mipmap.ic_splash_bg), null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
    LaunchedEffect(true) {
        scopeNet {
            Post<LoginModel>(Api.LOGIN_STATUS).await().also {
                LogUtils.d("账号状态为 -> $it")
                if (it.profile != null) {
                    // 登录状态为已登录
                    this@LaunchedEffect.launch(Dispatchers.IO) {
                        DataBaseManager.db?.userDao?.install(it)
                    }
                    viewModel?.state?.let {state ->
                        state.isLogin.value = true
                        state.isShowBottomTab.value = true
                        state.goPageType.value = PAGE_TYPE.HOME
                    }
                    navController?.navigate(RouteConstant.HOME) {
                        popUpTo(RouteConstant.SPLASH_PAGE) { inclusive = true }
                    }
                } else {
                    // 登录状态为未登录
                    viewModel?.state?.let {state ->
                        state.goPageType.value = PAGE_TYPE.LOGIN
                    }
                    navController?.navigate(RouteConstant.LOGIN) {
                        popUpTo(RouteConstant.SPLASH_PAGE) { inclusive = true }
                    }
                }
            }
        }
    }
}