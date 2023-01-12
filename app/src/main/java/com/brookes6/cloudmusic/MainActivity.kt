package com.brookes6.cloudmusic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.HomePage
import com.brookes6.cloudmusic.ui.page.LoginPage
import com.brookes6.cloudmusic.ui.page.PhoneLoginPage
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
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = RouteConstant.LOGIN_PAGE){
                composable(RouteConstant.LOGIN_PAGE){
                    LoginPage(navController)
                }
                composable(RouteConstant.PHONE_LOGIN_PAGE){
                    PhoneLoginPage(navController)
                }
                composable(RouteConstant.HOME_PAGE){
                    HomePage(navController)
                }
            }
        }
    }

}