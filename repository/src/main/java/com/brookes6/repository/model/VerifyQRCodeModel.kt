package com.brookes6.repository.model

import kotlinx.serialization.Serializable

/**
 * Author: fuxinbo

 * Date: 2023/2/19

 * Description:
 */
@Serializable
data class VerifyQRCodeModel(
    val code: Int,
    val message: String?,
    val cookie: String?
)
