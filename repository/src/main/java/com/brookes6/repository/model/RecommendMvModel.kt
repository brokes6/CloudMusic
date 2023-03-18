package com.brookes6.repository.model

/**
 * Author: Sakura

 * Date: 2023/3/17

 * Description: 推荐MV实体类
 */
@kotlinx.serialization.Serializable
data class RecommendMvModel(
    val code: Int = 0,
    val category: Int = 0,
    val result: List<RecommendMvInfo?>
)

@kotlinx.serialization.Serializable
data class RecommendMvInfo(
    val id: Long = 0,
    val type: Int = 0,
    val name: String = "",
    val copywriter: String = "",
    val picUrl: String = "",
    val canDislike: Boolean = false,
    val duration: Long = 0,
    val playCount: Long = 0,
    val artists: List<MvArtistsInfo?>,
    val artistName: String = "",
    var url: String = ""
)

@kotlinx.serialization.Serializable
data class MvArtistsInfo(
    val id: Long = 0,
    val name: String = ""
)

@kotlinx.serialization.Serializable
data class MvUrlModel(
    val code: Int = 0,
    val data: MvUrlInfo?
)

@kotlinx.serialization.Serializable
data class MvUrlInfo(
    val id: Long = 0,
    val url: String = "",
    val r: Int = 0,
    val size: Int = 0,
    val fee: Int = 0,
    val msg: String = ""
)