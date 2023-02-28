package com.brookes6.cloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.*
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.*
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.drake.brv.utils.BRV
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BRV.modelId = BR.data
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            rememberSystemUiController().run {
                setNavigationBarColor(Color.Transparent, false)
                setSystemBarsColor(Color.Transparent, false)
            }
            val viewModel: MainViewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel()
            val homeViewModel: HomeViewModel = viewModel()

            val navController = rememberAnimatedNavController()
            val state = rememberBottomSheetScaffoldState()
            BottomSheetScaffold(
                sheetContent = {
                    SongDetailPage(viewModel = viewModel, activity = this@MainActivity)
                },
                scaffoldState = state,
                sheetPeekHeight = 0.dp,
                sheetShape = RoundedCornerShape(20.dp, 20.dp, 0.dp, 0.dp)
            ) {
                ConstraintLayout(
                    Modifier
                        .background(mainBackground)
                        .fillMaxSize()
                ) {
                    val (content, songController, bottomTab) = createRefs()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = RouteConstant.SPLASH_PAGE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(content) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(bottomTab.top)
                                height = Dimension.fillToConstraints
                            }
                    ) {
                        composable(RouteConstant.SPLASH_PAGE) {
                            SplashPage(
                                navController,
                                viewModel
                            )
                        }
                        loginGraph(navController, viewModel, loginViewModel)
                        homeGraph(navController, homeViewModel, viewModel)
                    }
                    AnimatedVisibility(
                        visible = viewModel.state.isShowSongController.value,
                        modifier = Modifier.constrainAs(songController) {
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(bottomTab.top)
                        }
                    ) {
                        SongController(
                            modifier = Modifier,
                            viewModel = viewModel
                        )
                    }
                    if (viewModel.state.isShowBottomTab.value) {
                        BottomTab(
                            modifier = Modifier
                                .padding(20.dp, 0.dp, 20.dp, 0.dp)
                                .navigationBarsPadding()
                                .fillMaxWidth()
                                .background(
                                    secondaryBackground,
                                    RoundedCornerShape(20.dp)
                                )
                                .padding(4.dp)
                                .constrainAs(bottomTab) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                },
                            viewModel.getHomeBottomTabList()
                        ) { route ->
                            navController.navigate(route) {
                                if (route != viewModel.state.currentRoute.value) {
                                    viewModel.state.currentRoute.value = route
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
            LaunchedEffect(key1 = viewModel.state.isShowSongDetailPage.value, block = {
                if (viewModel.state.isInitPage.value) {
                    viewModel.state.isInitPage.value = false
                    return@LaunchedEffect
                }
                if (state.bottomSheetState.isExpanded) {
                    LogUtils.d("折叠音乐详情页")
                    state.bottomSheetState.collapse()
                } else {
                    LogUtils.d("展开音乐详情页")
                    state.bottomSheetState.expand()
                }
            })
        }
    }

    /**
     * 登陆页导航图
     *
     * @param navController
     */
    @OptIn(ExperimentalAnimationApi::class)
    private fun NavGraphBuilder.loginGraph(
        navController: NavController,
        viewModel: MainViewModel,
        loginViewModel: LoginViewModel
    ) {
        navigation(startDestination = RouteConstant.LOGIN_PAGE, route = RouteConstant.LOGIN) {
            composable(RouteConstant.LOGIN_PAGE) {
                LoginPage(onNavController = { page -> navController.navigate(page) })
            }
            composable(RouteConstant.PHONE_CODE_PAGE) {
                PhoneCodePage(
                    loginViewModel,
                    onNavController = { page ->
                        navController.navigate(page) {
                            viewModel.state.isShowBottomTab.value = true
                            viewModel.state.isLogin.value = true
                            popUpTo(RouteConstant.LOGIN_PAGE) { inclusive = true }
                        }
                    })
            }
            composable(RouteConstant.PHONE_QRCODE_PAGE) {
                QRCodePage(loginViewModel,
                    onNavController = { page ->
                        navController.navigate(page) {
                            viewModel.state.isShowBottomTab.value = true
                            viewModel.state.isLogin.value = true
                            popUpTo(RouteConstant.LOGIN_PAGE) { inclusive = true }
                        }
                    })
            }
            composable(RouteConstant.PHONE_LOGIN_PAGE) {
                PhoneLoginPage(loginViewModel, onNavController = { page ->
                    when (page) {
                        RouteConstant.HOME_PAGE -> {
                            viewModel.state.isShowBottomTab.value = true
                            viewModel.state.isLogin.value = true
                            navController.navigate(page) {
                                popUpTo(RouteConstant.LOGIN_PAGE) { inclusive = true }
                            }
                        }
                        else -> {
                            navController.navigate(page)
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
    private fun NavGraphBuilder.homeGraph(
        navController: NavController,
        homeViewModel: HomeViewModel,
        viewModel: MainViewModel
    ) {
        navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
            composable(RouteConstant.HOME_PAGE) {
                HomePage(navController, homeViewModel, viewModel)
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