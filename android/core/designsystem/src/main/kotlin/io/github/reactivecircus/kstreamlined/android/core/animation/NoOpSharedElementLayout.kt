package io.github.reactivecircus.kstreamlined.android.core.animation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable

@Composable
public fun NoOpSharedElementLayout(
    content: @Composable context(SharedTransitionScope, AnimatedVisibilityScope) () -> Unit,
) {
    SharedTransitionLayout {
        AnimatedVisibility(
            visible = true,
            enter = EnterTransition.None,
            exit = ExitTransition.None,
        ) {
            content()
        }
    }
}
