package com.brookes6.cloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.HomePage
import com.brookes6.cloudmusic.ui.page.LoginPage
import com.brookes6.cloudmusic.ui.page.PhoneLoginPage
import com.drake.serialize.serialize.deserialize
import com.drake.statusbar.immersive

/**
 * Author: fuxinbo

 * Date: 2023/1/12

 * Description:
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        immersive(darkMode = false)
        val isLogin: Boolean = deserialize("isLogin", false)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = if (isLogin) RouteConstant.HOME_PAGE else RouteConstant.LOGIN_PAGE
            ) {
                loginGraph(navController)
                homeGraph(navController)
            }
        }
    }

    /**
     * 登陆页导航图
     *
     * @param navController
     */
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
    private fun NavGraphBuilder.homeGraph(navController: NavController) {
        navigation(startDestination = RouteConstant.HOME_PAGE, route = RouteConstant.HOME) {
            composable(RouteConstant.HOME_PAGE) {
                HomePage(navController)
            }
        }
    }

}