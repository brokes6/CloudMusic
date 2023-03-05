package com.brookes6.repository.model

/**
 * Author: fuxinbo

 * Date: 2023/3/4

 * Description:
 */
@kotlinx.serialization.Serializable
data class CookieModel(
    val msg : String? = "",
    val code : Int = 0,
    val message : String? = "",
    val cookie : String? = ""
)
