package com.brookes6.cloudmusic.ui.widget

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeableDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.CancellationException


@Stable
class BottomSheetDialogState(
    initialValue: BottomSheetDialogValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BottomSheetDialogValue) -> Boolean = { true }
) : DialogSwipeableState<BottomSheetDialogValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {
    /**
     * Whether the bottom sheet is expanded.
     */
    val isExpanded: Boolean
        get() = currentValue == BottomSheetDialogValue.Expanded

    /**
     * Whether the bottom sheet is collapsed.
     */
    val isCollapsed: Boolean
        get() = currentValue == BottomSheetDialogValue.Collapsed

    /**
     * Expand the bottom sheet with animation and suspend until it if fully expanded or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the expand animation ended
     */
    suspend fun expand() = animateTo(BottomSheetDialogValue.Expanded)

    /**
     * Collapse the bottom sheet with animation and suspend until it if fully collapsed or animation
     * has been cancelled. This method will throw [CancellationException] if the animation is
     * interrupted
     *
     * @return the reason the collapse animation ended
     */
    suspend fun collapse() = animateTo(BottomSheetDialogValue.Collapsed)

    suspend fun hide() = animateTo(BottomSheetDialogValue.Hide)

    companion object {
        /**
         * The default [Saver] implementation for [BottomSheetDialogStateDialog].
         */
        @OptIn(ExperimentalMaterialApi::class)
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (BottomSheetDialogValue) -> Boolean
        ): Saver<BottomSheetDialogState, *> = Saver(save = { it.currentValue }, restore = {
            BottomSheetDialogState(
                initialValue = it,
                animationSpec = animationSpec,
                confirmStateChange = confirmStateChange
            )
        })
    }

    internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection
}

/**
 * Create a [BottomSheetDialogStateDialog] and [remember] it.
 *
 * @param initialValue The initial value of the state.
 * @param animationSpec The default animation that will be used to animate to a new state.
 * @param confirmStateChange Optional callback invoked to confirm or veto a pending state change.
 */
@Composable
fun rememberBottomSheetDialogState(
    initialValue: BottomSheetDialogValue,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
    confirmStateChange: (BottomSheetDialogValue) -> Boolean = { true }
): BottomSheetDialogState {
    return rememberSaveable(
        animationSpec, saver = BottomSheetDialogState.Saver(
            animationSpec = animationSpec, confirmStateChange = confirmStateChange
        )
    ) {
        BottomSheetDialogState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}