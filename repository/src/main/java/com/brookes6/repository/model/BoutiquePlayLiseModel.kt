package com.brookes6.repository.model

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description:
 */
@kotlinx.serialization.Serializable
data class BoutiquePlayLiseModel(
    val playlists: List<BoutiquePlayLiseInfo?>,
    val code: Int? = 0,
)

@kotlinx.serialization.Serializable
data class BoutiquePlayLiseInfo(
    val name: String? = "",
    val id: Long? = 0,
    val coverImgUrl: String? = "",
    val description: String? = "",
    val playCount: Long? = 0,
    val tag: String? = "",
    val commentCount: Long? = 0,
    val creator : CreatorInfo?
)
