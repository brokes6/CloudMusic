package com.brookes6.repository.model

import kotlinx.serialization.Serializable


/**
 * @Author fuxinbo
 * @Date 2023/1/17 0:50
 * @Description TODO
 */
@Serializable
data class RecommendSongModel(
    val dailySongs: MutableList<DailySongs>? = null,
    val code : Int = 0,
) {

    @Serializable
    data class DailySongs(
        val name: String = "",
        val id: Long = 0,
        val reason: String = "",
        /**
         *   0: 免费或无版权
         *   1: VIP 歌曲
         *   4: 购买专辑
         *   8: 非会员可免费播放低音质，会员可播放高音质及下载
         *   fee 为 1 或 8 的歌曲均可单独购买 2 元单曲
         */
        val fee: Int = 0,
        val alg: String = "",
        val ar: List<Author?>? = null,
        val al: SongImage? = null,
        val mark: Long = 0,
    )

    @Serializable
    data class Author(
        val name: String = ""
    )

    @Serializable
    data class SongImage(
        val id: Long = 0,
        val name: String = "",
        val picUrl: String = "",
    )
}
