package com.brookes6.net.api

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
object Api {

    /**
     * TODO
     * 不更新IP地址
     */
    const val BASE_URL = "https://service-norc4cei-1300670965.gz.apigw.tencentcs.com/release"

    /**
     * 刷新登录
     *
     * 说明 : 调用此接口 , 可刷新登录状态,返回内容包含新的cookie(不支持刷新二维码登录的cookie)
     */
    const val REFRESH_COOKIE = "/login/refresh"

    /**
     * 手机登录
     */
    const val PHONE_LOGIN = "/login/cellphone"

    /**
     * 发送验证码
     *
     * 说明 : 调用此接口 ,传入手机号码, 可发送验证码
     */
    const val SEND_PHONE_CODE = "/captcha/sent"

    /**
     * 验证验证码
     *
     * 说明 : 调用此接口 ,传入手机号码和验证码, 可校验验证码是否正确
     */
    const val VERIFY_PHONE_CODE = "/captcha/verify"

    /**
     * 二维码 key 生成接口
     *
     * 说明: 调用此接口可生成一个 key
     */
    const val GENERATE_RQCODE_KEY = "/login/qr/key"

    /**
     * 二维码生成接口
     *
     * 说明: 调用此接口传入上一个接口生成的 key 可生成二维码图片的 base64 和二维码信息,
     * 可使用 base64 展示图片,或者使用二维码信息内容自行使用第三方二维码生成库渲染二维码
     * - 必选参数: key,由第一个接口生成
     * - 可选参数: qrimg 传入后会额外返回二维码图片 base64 编码
     */
    const val CREATE_RQCODE = "/login/qr/create"

    /**
     * 二维码检测扫码状态接口
     *
     * 说明: 轮询此接口可获取二维码扫码状态,800 为二维码过期,801
     * 为等待扫码,802 为待确认,803 为授权登录成功(803 状态码下会返回 cookies)
     */
    const val VERIFY_QRCODE = "/login/qr/check"

    /**
     * 登录状态
     *
     * 说明 : 调用此接口,可获取登录状态
     */
    const val LOGIN_STATUS = "/login/status"

    /**
     * 获取账号信息
     *
     * 说明 : 登录后调用此接口 ,可获取用户账号信息
     */
    const val ACCOUNT_INFO = "/user/account"

    /**
     * 获取每日推荐歌曲
     *
     * 说明 : 调用此接口 , 可获得每日推荐歌曲 ( 需要登录 )
     */
    const val RECOMMEND_SONG = "/recommend/songs"

    /**
     * 获取音乐 url - 新版
     *
     * 说明 : 使用歌单详情接口后 , 能得到的音乐的 id, 但不能得到的音乐 url, 调用此接口, 传入的音乐 id( 可多个 , 用逗号隔开 ),
     * 可以获取对应的音乐的 url,未登录状态或者非会员返回试听片段(返回字段包含被截取的正常歌曲的开始时间和结束时间)
     *
     * 必选参数 : id : 音乐 id
     *
     * 可选参数 : br: 码率,默认设置了 999000 即最大码率,如果要 320k 则可设置为 320000,其他类推
     */
    const val GET_MUSIC_URL = "/song/url/v1"

    /**
     * 获取歌词
     *
     * 说明 : 调用此接口 , 传入音乐 id 可获得对应音乐的歌词 ( 不需要登录 )
     * - 必选参数 : id: 音乐 id
     */
    const val GET_MUSIC_LYRIC = "/lyric"

    /**
     * 获取用户详情
     *
     * 说明 : 登录后调用此接口 , 传入用户 id, 可以获取用户详情
     * - 必选参数 : uid : 用户 id
     */
    const val GET_USER_INFO = "/user/detail"

    /**
     * 获取用户歌单
     *
     * 说明 : 登录后调用此接口 , 传入用户 id, 可以获取用户歌单
     * - 必选参数 : uid : 用户 id
     *
     * 可选参数 :
     * - limit : 返回数量 , 默认为 30
     * - offset : 偏移数量，用于分页 , 如 :( 页数 -1)*30, 其中 30 为 limit 的值 , 默认为 0
     */
    const val GET_USER_PLAYLIST = "/user/playlist"

    /**
     * 获取歌单所有歌曲
     *
     * 说明 : 由于网易云接口限制，歌单详情只会提供 10 首歌，通过调用此接口，传入对应的歌单id，即可获得对应的所有歌曲
     *
     * 必选参数
     * - id : 歌单 id
     *
     * 可选参数
     * - limit : 限制获取歌曲的数量，默认值为当前歌单的歌曲数量
     * - offset : 默认值为0
     */
    const val GET_PLAY_LIST_DETAIL = "/playlist/track/all"

    /**
     * 喜欢音乐列表
     *
     * 说明 : 调用此接口 , 传入用户 id, 可获取已喜欢音乐 id 列表(id 数组)
     * - 必选参数 : uid: 用户 id
     */
    const val GET_LIkE_LIST = "/likelist"

    /**
     * 喜欢音乐
     *
     * 说明 : 调用此接口 , 传入音乐 id, 可喜欢该音乐
     * - 必选参数 : id: 歌曲 id
     *
     * 可选参数 :
     * - like: 布尔值 , 默认为 true 即喜欢 , 若传 false, 则取消喜欢
     */
    const val LIST_OR_DISLIKE_SONG = "/like"

    /**
     * 推荐 mv
     *
     * 说明 : 调用此接口 , 可获取推荐 mv
     */
    const val GET_RECOMMEND_MV = "/personalized/mv"
}