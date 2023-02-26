package com.brookes6.net.api

/**
 * Author: fuxinbo

 * Date: 2023/1/11

 * Description:
 */
object Api {

    const val BASE_URL = "http://192.168.31.133:3000"

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
}