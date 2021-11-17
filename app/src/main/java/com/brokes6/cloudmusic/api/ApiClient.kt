package com.brokes6.cloudmusic.api

import com.brokes6.cloudmusic.api.service.ApiService
import com.brokes6.cloudmusic.constants.ApiConstants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Author: 付鑫博
 * Version: 1.0.0
 * Date: 2021/11/17
 * Mender:
 * Modify:
 * Description:
 */
class ApiClient private constructor() {

    companion object {
        val instance: ApiClient by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { ApiClient() }
    }

    private val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    fun api(): ApiService {
        return instance.create(ApiService::class.java)
    }

}