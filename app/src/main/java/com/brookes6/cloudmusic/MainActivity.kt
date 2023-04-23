package com.brookes6.cloudmusic

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.ui.page.*
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.*
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.MyViewModel
import com.brookes6.cloudmusic.vm.TokenViewModel
import com.brookes6.cloudmusic.vm.UserViewModel
import com.drake.brv.utils.BRV
import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.lzx.starrysky.OnPlayProgressListener
import com.lzx.starrysky.StarrySky
import com.lzx.starrysky.StarrySkyInstall
import com.lzx.starrysky.manager.PlaybackStage

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MainActivity : FragmentActivity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var content: FragmentActivity

        @JvmStatic
        lateinit var viewModel: MainViewModel
    }

    private val mUserVM: UserViewModel by viewModels()

    private val mTokenVM: TokenViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = this
        BRV.modelId = BR.data
        WindowCompat.setDecorFitsSystemWindows(window, false)
        initObserve()

        // UI部分
        setContent {
            rememberSystemUiController().run {
                setNavigationBarColor(Color.Transparent, false)
                setSystemBarsColor(Color.Transparent, false)
            }
            viewModel = viewModel()
            val loginViewModel: LoginViewModel = viewModel()
            val homeViewModel: HomeViewModel = viewModel()
            val myViewModel: MyViewModel = viewModel()

            val navController = rememberAnimatedNavController()
            val state = rememberBottomSheetScaffoldState()
            BottomSheetScaffold(
                sheetContent = {
                    SongDetailPage(viewModel = viewModel)
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
                    val (content, bottomTab) = createRefs()
                    AnimatedNavHost(
                        navController = navController,
                        startDestination = RouteConstant.SPLASH_PAGE,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(content) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
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
                                viewModel.getHomeBottomTabList(),
                                viewModel
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

        /**
         * 播放控制监听
         */
        StarrySkyInstall.onStarryInitSuccessCallback = {
            StarrySky.with()?.playbackState()?.observe(this) { play ->
                when (play.stage) {
                    PlaybackStage.IDLE -> {
                        if (!play.isStop) {
                            viewModel.state.mProgress.value = 0f
                        }
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.NOMAL
                    }

                    PlaybackStage.SWITCH -> {
                        viewModel.dispatch(MainAction.GetCurrentSong)
                    }

                    PlaybackStage.PAUSE -> {
                        LogUtils.i("当前音乐已暂停", "Song")
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.PAUSE
                    }

                    PlaybackStage.PLAYING -> {
                        LogUtils.i("当前音乐正在播放", "Song")
                        viewModel.state.mPlayStatus.value = PLAY_STATUS.PLAYING
                        if (viewModel.state.isShowSongController.value) return@observe
                        LogUtils.i("检测到当前正在播放音乐，将显示音乐控制器", "Song")
                        viewModel.state.isShowSongController.value = true
                    }

                    PlaybackStage.ERROR -> {
                        LogUtils.e("当前音乐出现错误 --> ${play.errorMsg}", "Song")
                    }
                }
            }
        }

        /**
         * 播放进度监听
         */
        StarrySky.with()?.setOnPlayProgressListener(object : OnPlayProgressListener {
            override fun onPlayProgress(currPos: Long, duration: Long) {
                viewModel.state.mCurrentPlayTime.value = currPos
                var progress = currPos.toFloat() / duration.toFloat()
                if (progress.isNaN()) progress = 0f
                viewModel.state.mProgress.value = progress
            }
        })
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
                    mTokenVM,
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
                PhoneLoginPage(loginViewModel, mTokenVM, onNavController = { page ->
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
                MyPage(myViewModel, mUserVM, onNavController = { page ->
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

    /**
     * 监听Token变化，当变化时，刷新用户信息
     */
    private fun initObserve() {
        mTokenVM.token.observe(this) {
            val origin: String? = deserialize(AppConstant.COOKIE)
            if (origin.isNullOrEmpty() || origin != it.cookie) {
                serialize(AppConstant.COOKIE to it.cookie)
                mUserVM.dispatch(MyAction.GetUserInfo)
            }
        }
    }
}