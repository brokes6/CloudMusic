package com.cloudmusic.lib_net.api.service

import com.cloudmusic.lib_net.api.bean.LoginBean
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
interface ApiService {

    @GET("/login/cellphone")
    suspend fun login(@Query("phone") phone: String, @Query("password") password: String) : LoginBean

}