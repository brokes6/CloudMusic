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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.*
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.BottomTab
import com.brookes6.cloudmusic.ui.widget.SongController
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
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
                    startDestination = RouteConstant.SPLASH_PAGE,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    composable(RouteConstant.SPLASH_PAGE) { SplashPage(navController, viewModel) }
                    loginGraph(navController, viewModel)
                    homeGraph(navController, viewModel)
                }
                AnimatedVisibility(visible = viewModel.state.isShowSongController.value) {
                    SongController(
                        modifier = Modifier,
                        activity = this@MainActivity
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
                    ) { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
    private fun NavGraphBuilder.loginGraph(navController: NavController, viewModel: MainViewModel) {
        navigation(startDestination = RouteConstant.LOGIN_PAGE, route = RouteConstant.LOGIN) {
            composable(RouteConstant.LOGIN_PAGE) {
                LoginPage(onNavController = { page -> navController.navigate(page) })
            }
            composable(RouteConstant.PHONE_CODE_PAGE){
                PhoneCodePage(onNavController = { page -> navController.navigate(page) })
            }
            composable(RouteConstant.PHONE_LOGIN_PAGE) {
                PhoneLoginPage(onNavController = { page ->
                    when(page){
                        RouteConstant.PHONE_CODE_PAGE ->{
                            navController.navigate(page)
                        }
                        RouteConstant.HOME_PAGE ->{
                            viewModel.state.isShowBottomTab.value = true
                            viewModel.state.isLogin.value = true
                            navController.navigate(page) {
                                popUpTo(RouteConstant.LOGIN_PAGE) { inclusive = true }
                            }
                        }
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
    private fun NavGraphBuilder.homeGraph(navController: NavController, viewModel: MainViewModel) {
        navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
            composable(RouteConstant.HOME_PAGE) {
                HomePage(navController, viewModel<HomeViewModel>())
            }
            composable(RouteConstant.HOME_SONG_PAGE) {
                SongPage()
            }
            composable(RouteConstant.HOME_SEARCH_PAGE) {
                SearchPage()
            }
        }
    }
}