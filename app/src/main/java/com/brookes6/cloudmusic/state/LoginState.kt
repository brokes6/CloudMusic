package com.brookes6.cloudmusic.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
data class LoginState(
    val isLogin : MutableState<Boolean> = mutableStateOf(false),
    val isError : MutableState<Boolean> = mutableStateOf(false),
    val code : MutableState<Int> = mutableStateOf(0),
    val message : MutableState<String> = mutableStateOf(""),
    val isShowPhoneCode : MutableState<Boolean> = mutableStateOf(false),
    val mQRCodeStatus : MutableState<Boolean> = mutableStateOf(false),
    val mQRCodeMessage : MutableState<String> = mutableStateOf("")
)