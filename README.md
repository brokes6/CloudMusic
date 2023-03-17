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

