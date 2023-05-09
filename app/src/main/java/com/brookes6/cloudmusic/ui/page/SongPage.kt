package com.brookes6.cloudmusic.ui.page

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.brookes6.cloudmusic.R
import com.brookes6.cloudmusic.action.SongPageAction
import com.brookes6.cloudmusic.constant.AppConstant.PLAY_LIST_INFO
import com.brookes6.cloudmusic.constant.RouteConstant
import com.brookes6.cloudmusic.extensions.navigateAndArgument
import com.brookes6.cloudmusic.ui.view.SquareLayoutManager
import com.brookes6.cloudmusic.vm.MainViewModel
import com.brookes6.cloudmusic.vm.SongPageViewModel
import com.brookes6.repository.model.BoutiquePlayLiseInfo
import com.brookes6.repository.model.PlayListInfo
import com.drake.brv.utils.models
import com.drake.brv.utils.setup

/**
 * @Author fuxinbo
 *
 * @Date 2023/1/17 14:21
 *
 * @Description TODO
 */
@Composable
fun SongPage(
    navController: NavController? = null,
    mainVM: MainViewModel,
    songPageViewModel: SongPageViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .navigationBarsPadding()
            .fillMaxSize()
    ) {
        AndroidView(
            factory = { context ->
                RecyclerView(context).apply {
                    overScrollMode = View.OVER_SCROLL_NEVER
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    layoutManager = SquareLayoutManager(5).apply {
                        isAutoSelect = true
                        isInitLayoutCenter = true
                    }
                    this.setup {
                        addType<BoutiquePlayLiseInfo>(R.layout.item_boutique_play_list)
                        onBind {
                            getModel<BoutiquePlayLiseInfo>(modelPosition).let { song ->
                                findView<ImageView>(R.id.imageCover).load(song.coverImgUrl) {
                                    transformations(RoundedCornersTransformation(20f))
                                }
                                findView<TextView>(R.id.textName).text = song.name
                            }
                        }
                        onClick(R.id.roots) {
                            mainVM.state.isShowBottomTab.value = false
                            getModel<BoutiquePlayLiseInfo>(modelPosition).let { song ->
                                navController?.navigateAndArgument(
                                    RouteConstant.SONG_PLAY_LIST,
                                    PLAY_LIST_INFO to PlayListInfo(
                                        name = song.name,
                                        id = song.id,
                                        coverImgUrl = song.coverImgUrl,
                                        creator = song.creator,
                                        description = song.description,
                                        playCount = null,
                                        trackCount = null
                                    )
                                )
                            }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxSize(),
            update = {
                it.models = songPageViewModel.playList.value
            }
        )
    }
    LaunchedEffect(key1 = songPageViewModel, block = {
        songPageViewModel.dispatch(SongPageAction.GetSongPageData)
    })
}