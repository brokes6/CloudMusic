package com.brookes6.cloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.inputmethod.InputConnectionCompat.OnCommitContentListener
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.BottomTab
import com.brookes6.cloudmusic.ui.page.HomePage
import com.brookes6.cloudmusic.ui.page.LoginPage
import com.brookes6.cloudmusic.ui.page.PhoneLoginPage
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.SongController
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.net.api.Api
import com.brookes6.repository.model.LoginModel
import com.drake.net.Get
import com.drake.net.utils.scopeNet
import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize
import com.drake.statusbar.immersive
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersive(darkMode = false)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val navController = rememberAnimatedNavController()
            Column(Modifier.background(mainBackground)) {
                AnimatedNavHost(
                    navController = navController,
                    startDestination = if (viewModel.state.isShowHomePage.value) RouteConstant.HOME else RouteConstant.LOGIN,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    loginGraph(navController)
                    homeGraph(navController)
                }
                AnimatedVisibility(visible = viewModel.state.isShowSongController.value) {
                    SongController(
                        modifier = Modifier
                    )
                }
                if (viewModel.state.isShowBottomTab.value) {
                    BottomTab(
                        modifier = Modifier
                            .padding(20.dp, 0.dp, 20.dp, 14.dp)
                            .fillMaxWidth()
                            .background(
                                secondaryBackground,
                                RoundedCornerShape(20.dp)
                            )
                            .padding(4.dp),
                        viewModel.getHomeBottomTabList()
                    )
                }
            }
            LaunchedEffect(this) {
                scopeNet {
                    Get<LoginModel>(Api.LOGIN_STATUS).await().also {
                        LogUtils.d("账号状态为 -> $it")
                        if (it.loginType == -1) {
                            viewModel.dispatch(MainViewModel.MainAction.GoHomePage)
                        }
                    }
                }
            }
        }
    }

    /**
     * 登陆页导航图
     *
     * @param navController
     */
    @OptIn(ExperimentalAnimationApi::class)
    private fun NavGraphBuilder.loginGraph(navController: NavController) {
        navigation(startDestination = RouteConstant.LOGIN_PAGE, route = RouteConstant.LOGIN) {
            composable(RouteConstant.LOGIN_PAGE) {
                LoginPage(onNavController = { page -> navController.navigate(page) })
            }
            composable(RouteConstant.PHONE_LOGIN_PAGE) {
                PhoneLoginPage(onNavController = { page ->
                    navController.navigate(page) {
                        popUpTo(RouteConstant.LOGIN_PAGE) { inclusive = true }
                    }
                })
            }
        }
    }

    /**
     * 主页导航图
     *
     * @param navController
     */
    @OptIn(ExperimentalAnimationApi::class)
    private fun NavGraphBuilder.homeGraph(navController: NavController) {
        navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
            composable(RouteConstant.HOME_PAGE) {
                HomePage(navController)
            }
        }
    }

}