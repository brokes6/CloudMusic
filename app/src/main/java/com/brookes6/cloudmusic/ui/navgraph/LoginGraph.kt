package com.brookes6.cloudmusic.ui.navgraph

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.ui.page.LoginPage
import com.brookes6.cloudmusic.ui.page.PhoneCodePage
import com.brookes6.cloudmusic.ui.page.PhoneLoginPage
import com.brookes6.cloudmusic.ui.page.QRCodePage
import com.brookes6.cloudmusic.vm.LoginViewModel
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.TokenViewModel

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description: 登陆页导航图
 */
fun NavGraphBuilder.loginGraph(
    navController: NavController,
    mTokenVM: TokenViewModel,
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
            QRCodePage(
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