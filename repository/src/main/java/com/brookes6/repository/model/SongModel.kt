package com.brookes6.repository.model

import kotlinx.serialization.Serializable

/**
 * @Author fuxinbo
 * @Date 2023/1/17 1:12
 * @Description TODO
 */
@Serializable
data class SongModel(
    val id: Long = 0,
    val url: String = "",
    val fee: Int = -1,
    val time: Long = 0,
)
