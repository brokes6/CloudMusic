package com.brookes6.cloudmusic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.*
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.*
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.MyViewModel
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
class MainActivity : FragmentActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var content: FragmentActivity
    }

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = this
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
            val myViewModel: MyViewModel = viewModel()

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
                        homeGraph(navController, homeViewModel, viewModel, myViewModel)
                        songGraph(navController, viewModel, myViewModel)
                    }
                    Column(
                        Modifier
                            .padding(20.dp, 0.dp, 20.dp, 0.dp)
                            .navigationBarsPadding()
                            .fillMaxWidth()
                            .animateContentSize()
                            .constrainAs(bottomTab) {
                                bottom.linkTo(parent.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (viewModel.state.isShowSongController.value) {
                            SongController(
                                modifier = Modifier,
                                viewModel = viewModel
                            )
                        }
                        if (viewModel.state.isShowBottomTab.value) {
                            BottomTab(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        secondaryBackground,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .padding(4.dp),
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
        viewModel: MainViewModel,
        myViewModel: MyViewModel
    ) {
        navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
            composable(RouteConstant.HOME_PAGE) {
                HomePage(navController, homeViewModel, viewModel)
            }
            composable(RouteConstant.HOME_SONG_PAGE) {
                SongPage()
            }
            composable(RouteConstant.HOME_MY_PAGE) {
                MyPage(myViewModel, onNavController = { page ->
                    viewModel.state.isShowBottomTab.value = false
                    navController.navigate(page)
                })
            }
        }
    }

    @OptIn(ExperimentalAnimationApi::class)
    private fun NavGraphBuilder.songGraph(
        navController: NavController,
        mainViewModel: MainViewModel,
        viewModel: MyViewModel,
    ) {
        navigation(startDestination = RouteConstant.SONG_PLAY_LIST, route = RouteConstant.SONG) {
            composable(
                RouteConstant.SONG_PLAY_LIST + "/{${AppConstant.PLAY_LIST_INDEX}}",
                arguments = listOf(navArgument(AppConstant.PLAY_LIST_INDEX) {
                    type = NavType.IntType
                    defaultValue = 0
                })
            ) {
                PlayListPage(
                    it.arguments?.getInt(AppConstant.PLAY_LIST_INDEX) ?: 0,
                    viewModel,
                    onNavController = { page ->
                        when (page) {
                            AppConstant.ON_BACK -> {
                                mainViewModel.state.isShowBottomTab.value = true
                                navController.popBackStack()
                            }
                            else -> {
                                navController.navigate(page)
                            }
                        }
                    }
                )
            }
        }
    }
}