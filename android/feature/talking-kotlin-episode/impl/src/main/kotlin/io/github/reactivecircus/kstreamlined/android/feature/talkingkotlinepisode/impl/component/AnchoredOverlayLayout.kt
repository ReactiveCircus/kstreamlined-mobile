package io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
internal fun AnchoredOverlayLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    bottomBar: @Composable () -> Unit,
    overlay: @Composable () -> Unit,
) {
    Layout(
        contents = listOf(content, bottomBar, overlay),
        modifier = modifier,
    ) { measurables, constraints ->
        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val bottomBarPlaceables = measurables[BottomBarSlot]
            .map { it.measure(looseConstraints) }
        val bottomBarHeight = bottomBarPlaceables.maxOfOrNull { it.height } ?: 0

        val contentHeight = (constraints.maxHeight - bottomBarHeight).coerceAtLeast(0)
        val contentConstraints = constraints.copy(
            minWidth = constraints.maxWidth,
            minHeight = contentHeight,
            maxHeight = contentHeight,
        )
        val contentPlaceables = measurables[ContentSlot]
            .map { it.measure(contentConstraints) }

        val overlayPlaceables = measurables[OverlaySlot]
            .map { it.measure(looseConstraints) }
        val overlayWidth = overlayPlaceables.maxOfOrNull { it.width } ?: 0
        val overlayHeight = overlayPlaceables.maxOfOrNull { it.height } ?: 0

        layout(constraints.maxWidth, constraints.maxHeight) {
            contentPlaceables.forEach { it.placeRelative(x = 0, y = 0) }

            val overlayX = (constraints.maxWidth - overlayWidth) / 2
            val overlayY = (constraints.maxHeight - bottomBarHeight - overlayHeight).coerceAtLeast(0)
            overlayPlaceables.forEach {
                it.placeRelative(x = overlayX, y = overlayY)
            }

            val bottomBarY = constraints.maxHeight - bottomBarHeight
            bottomBarPlaceables.forEach {
                it.placeRelative(x = 0, y = bottomBarY)
            }
        }
    }
}

private const val ContentSlot = 0
private const val BottomBarSlot = 1
private const val OverlaySlot = 2
