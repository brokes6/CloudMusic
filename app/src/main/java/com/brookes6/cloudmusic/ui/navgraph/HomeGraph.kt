package com.brookes6.cloudmusic.ui.navgraph

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.HomePage
import com.brookes6.cloudmusic.ui.page.MyPage
import com.brookes6.cloudmusic.ui.page.SearchPage
import com.brookes6.cloudmusic.ui.page.SongPage
import com.brookes6.cloudmusic.vm.HomeViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.MyViewModel
import com.brookes6.cloudmusic.vm.SongPageViewModel
import com.brookes6.cloudmusic.vm.UserViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description: 主页导航图
 */
@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.homeGraph(
    navController: NavController,
    mUserVM: UserViewModel,
    homeViewModel: HomeViewModel,
    mainVM: MainViewModel,
    myViewModel: MyViewModel,
    songPageVM: SongPageViewModel
) {
    navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
        composable(RouteConstant.HOME_PAGE) {
            HomePage(navController, homeViewModel, mainVM, mUserVM)
        }
        composable(RouteConstant.HOME_SONG_PAGE) {
            SongPage(navController, mainVM, songPageVM)
        }
        composable(RouteConstant.HOME_MY_PAGE) {
            MyPage(myViewModel, mUserVM, mainVM, navController)
        }
        composable(RouteConstant.HOME_SEARCH_PAGE){
            SearchPage(mainVM,navController)
        }
    }
}