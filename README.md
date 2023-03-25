# CloudMusic
> MusicPlayer-Compose翻版</br>
> 目前正在开发中

## 整体思路
1. 网络请求
    - Net(https://github.com/liangjingkanji/Net) + 协程 + Flow + kotlin-serialization
2. 整体框架
    - MVI
3. 屏幕适配
    - Compose自适应
4. 数据保存
    - MMKV,Room
5. 音频播放
    - StarrySky
6. API
    - NeteaseCloudMusicApi
> 整体UI图则采用”即时设计“中的开源UI设计图 </br>
> https://js.design/community?category=detail&type=resource&id=6288a7ac555ed21fe570c692

## 完成进度:
- [x] 账号登陆
- [x] 验证码登陆
- [x] 二维码登陆
- [x] 每日推荐
- [x] 播放选中音频
- [ ] 推荐MV
- [ ] 搜索
- [ ] 个人页面
- [ ] 推荐音乐
- [x] 音乐播放页
- [ ] 我的歌单
- [x] 歌单详情页
- [ ] 歌手详情页
- [ ] 歌曲分类
- [ ] 播放列表

## 效果展示(展示已完成的部分):
图片会有一些拉伸，无法展示真实效果，如果可以请自行编译查看效果(自带腾讯云函数服务，可立即使用)


- 登录页部分

<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/startPage.png" width="33%" height="700"><img src="https://github.com/brokes6/CloudMusic/blob/main/photo/phoneLoginPage.png" width="33%" height="700"><img src="https://github.com/brokes6/CloudMusic/blob/main/photo/phoneCodePage.png" width="33%" height="700">
<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/phoneQRPage.png" width="33%" height="700">

- 首页部分

<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/homePage.png" width="33%" height="700"><img src="https://github.com/brokes6/CloudMusic/blob/main/photo/homePagePlay.png" width="33%" height="700">

- 音乐详情页部分

<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/songDetailPage.png" width="33%" height="700"><img src="https://github.com/brokes6/CloudMusic/blob/main/photo/songDetailLyricPage.png" width="33%" height="700">

- 我的页面部分

<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/myPage.png" width="33%" height="700">

- 歌单详情页部分

<img src="https://github.com/brokes6/CloudMusic/blob/main/photo/songListPage.png" width="33%" height="700">
