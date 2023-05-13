package com.brookes6.repository.model

import kotlinx.serialization.Serializable

/**
 * Author: Sakura

 * Date: 2023/5/11

 * Description:
 */
@Serializable
data class SearchHotDetailModel(
    val code: Int? = 0,
    val data: List<SearchHotDetailInfo?> = listOf(),
    val message: String? = ""
)

@Serializable
data class SearchHotDetailInfo(
    val searchWord: String? = "",
    val score: Int? = 0,
    val content: String? = "",
    val iconUrl: String? = "",
    val alg: String? = ""
)
