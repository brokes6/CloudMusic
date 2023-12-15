package com.brookes6.cloudmusic.ui.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.brookes6.cloudmusic.constant.AppConstant
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.PlayListPage
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.repository.model.PlayListInfo
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description:
 */
fun NavGraphBuilder.songGraph(
    navController: NavController,
    mainViewModel: MainViewModel,
) {
    navigation(startDestination = RouteConstant.SONG_PLAY_LIST, route = RouteConstant.SONG) {
        composable(RouteConstant.SONG_PLAY_LIST) {
            val json = it.arguments?.getString(AppConstant.PLAY_LIST_INFO)
            if (json.isNullOrEmpty()) return@composable
            val mPlayListInfo: PlayListInfo = Json.decodeFromString(json)
            PlayListPage(
                mainViewModel,
                mPlayListInfo,
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