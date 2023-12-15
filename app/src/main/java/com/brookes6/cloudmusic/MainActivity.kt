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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brookes6.cloudmusic.action.MainAction
import com.brookes6.cloudmusic.action.MyAction
import com.brookes6.cloudmusic.bean.enum.BottomDialogEnum
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.state.PLAY_STATUS
import com.brookes6.cloudmusic.ui.dialog.PlayListBottomDialog
import com.brookes6.cloudmusic.ui.navgraph.homeGraph
import com.brookes6.cloudmusic.ui.navgraph.loginGraph
import com.brookes6.cloudmusic.ui.navgraph.songGraph
import com.brookes6.cloudmusic.ui.page.*
import com.brookes6.cloudmusic.ui.theme.mainBackground
import com.brookes6.cloudmusic.ui.theme.secondaryBackground
import com.brookes6.cloudmusic.ui.widget.*
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.MyViewModel
import com.brookes6.cloudmusic.vm.SongPageViewModel
import com.brookes6.cloudmusic.vm.TokenViewModel
import com.brookes6.cloudmusic.vm.UserViewModel
import com.drake.brv.utils.BRV
import com.drake.serialize.serialize.deserialize
import com.drake.serialize.serialize.serialize
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
    }

    private val viewModel: MainViewModel by viewModels()

    private val mUserVM: UserViewModel by viewModels()

    private val mTokenVM: TokenViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        content = this
        BRV.modelId = BR.data
        WindowCompat.setDecorFitsSystemWindows(window, false)
        App.app.setTokenVM(mTokenVM)
        initObserve()
        mUserVM.dispatch(MyAction.GetUserInfo)
        // UI部分
        setContent {
            rememberSystemUiController().run {
                setNavigationBarColor(Color.Transparent, false)
                setSystemBarsColor(Color.Transparent, false)
            }
            val loginViewModel: LoginViewModel = viewModel()
            val homeViewModel: HomeViewModel = viewModel()
            val myViewModel: MyViewModel = viewModel()
            val songPageVM: SongPageViewModel = viewModel()

            val navController = rememberNavController()
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
                    val (content, bottomTab, dialog) = createRefs()
                    NavHost(
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
                                mTokenVM,
                                viewModel
                            )
                        }
                        loginGraph(navController, mTokenVM, viewModel, loginViewModel)
                        homeGraph(
                            navController,
                            mUserVM,
                            homeViewModel,
                            viewModel,
                            myViewModel,
                            songPageVM
                        )
                        songGraph(navController, viewModel)
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
                    // BottomDialog
                    AnimatedContent(
                        targetState = viewModel.state.mShowDialogType.value,
                        modifier = Modifier
                            .navigationBarsPadding()
                            .constrainAs(dialog) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                            },
                        transitionSpec = {
                            slideInVertically { it } with slideOut(targetOffset = {
                                IntOffset(
                                    it.width,
                                    -it.height
                                )
                            })
                        },
                        label = "BottomDialog",
                    ) {
                        when (it) {
                            BottomDialogEnum.PLAY_LIST_DIALOG.ordinal -> {
                                PlayListBottomDialog(viewModel)
                            }

                            else -> {}
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

        StarrySkyInstall.onStarryInitSuccessCallback = {
            /**
             * 播放控制监听
             */
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
    }

    /**
     * 监听Token变化，当变化时，刷新用户信息
     */
    private fun initObserve() {
        mTokenVM.token.observe(this) {
            val origin: String? = deserialize(AppConstant.COOKIE)
            // 不同的Token才可重新储存
            if (origin != it.cookie) {
                serialize(AppConstant.COOKIE to it.cookie)
            }
            // 只有Token不为空的时候，才进行用户信息获取
            if (!origin.isNullOrEmpty()) {
                mUserVM.dispatch(MyAction.GetUserInfo)
            }
        }
    }
}