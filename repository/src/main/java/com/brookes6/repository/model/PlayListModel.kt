package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/3/5

 * Description:
 */
@kotlinx.serialization.Serializable
data class PlayListModel(
    val more : Boolean = false,
    val playlist : MutableList<PlayListInfo?>,
)

@kotlinx.serialization.Serializable
data class PlayListInfo(
    val name : String = "",
    val id : Long = 0,
    val coverImgUrl : String = "",
    val creator : CreatorInfo?,
    val description : String = "",
    val playCount : Int,
    val trackCount : Int,

)
@kotlinx.serialization.Serializable
data class CreatorInfo(
    val avatarUrl : String = "",
    val nickname : String = "",
    val description : String = "",
)
