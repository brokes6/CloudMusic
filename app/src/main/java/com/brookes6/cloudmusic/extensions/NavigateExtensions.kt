package com.brookes6.cloudmusic.extensions

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.brookes6.cloudmusic.utils.LogUtils
import com.brookes6.repository.model.PlayListInfo
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Author: Sakura

 * Date: 2023/5/9

 * Description:
 */
fun NavController.navigateAndArgument(
    route: String,
    args: Pair<String, PlayListInfo?>,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    navigate(route = route, navOptions = navOptions, navigatorExtras = navigatorExtras)
    val bundle = backQueue.lastOrNull()?.arguments
    if (args.second == null) {
        LogUtils.e("bundle.putParcelable value is null")
        return
    }
    val json = Json.encodeToString(args.second)
    bundle?.putString(args.first, json)
}