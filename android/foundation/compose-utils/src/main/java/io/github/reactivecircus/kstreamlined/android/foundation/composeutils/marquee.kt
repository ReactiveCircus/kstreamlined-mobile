package io.github.reactivecircus.kstreamlined.android.foundation.composeutils

import androidx.compose.foundation.DefaultMarqueeDelayMillis
import androidx.compose.foundation.DefaultMarqueeIterations
import androidx.compose.foundation.DefaultMarqueeSpacing
import androidx.compose.foundation.DefaultMarqueeVelocity
import androidx.compose.foundation.MarqueeAnimationMode
import androidx.compose.foundation.MarqueeSpacing
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Stable
public fun Modifier.marqueeWithFadedEdges(
    fadedEdgeMode: FadedEdgeMode = FadedEdgeMode.Both,
    edgeWidth: Dp = DefaultEdgeWidth,
    startEdgePadding: Dp = edgeWidth,
    iterations: Int = DefaultMarqueeIterations,
    animationMode: MarqueeAnimationMode = MarqueeAnimationMode.Immediately,
    delayMillis: Int = DefaultMarqueeDelayMillis,
    initialDelayMillis: Int = if (animationMode == MarqueeAnimationMode.Immediately) delayMillis else 0,
    spacing: MarqueeSpacing = DefaultMarqueeSpacing,
    velocity: Dp = DefaultMarqueeVelocity
): Modifier = if (fadedEdgeMode != FadedEdgeMode.None) {
    offset(x = -startEdgePadding)
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
} else { this }
    .drawWithContent {
        drawContent()
        when (fadedEdgeMode) {
            FadedEdgeMode.Both -> {
                drawFadedEdge(
                    edgeWidth = edgeWidth,
                    leftEdge = true,
                )
                drawFadedEdge(
                    edgeWidth = edgeWidth,
                    leftEdge = false,
                )
            }

            FadedEdgeMode.Start -> drawFadedEdge(
                edgeWidth = edgeWidth,
                leftEdge = true,
            )

            FadedEdgeMode.End -> drawFadedEdge(
                edgeWidth = edgeWidth,
                leftEdge = false,
            )

            FadedEdgeMode.None -> Unit
        }
    }
    .basicMarquee(
        iterations = iterations,
        animationMode = animationMode,
        delayMillis = delayMillis,
        initialDelayMillis = initialDelayMillis,
        spacing = spacing,
        velocity = velocity
    )
    .then(
        if (fadedEdgeMode != FadedEdgeMode.None) {
            Modifier.padding(start = startEdgePadding)
        } else {
            Modifier
        }
    )

@JvmInline
public value class FadedEdgeMode private constructor(private val value: Int) {

    override fun toString(): String = when (this) {
        Both -> "Both"
        Start -> "Start"
        End -> "End"
        None -> "None"
        else -> error("invalid value: $value")
    }

    public companion object {

        public val Both: FadedEdgeMode = FadedEdgeMode(0)

        public val Start: FadedEdgeMode = FadedEdgeMode(1)

        public val End: FadedEdgeMode = FadedEdgeMode(2)

        public val None: FadedEdgeMode = FadedEdgeMode(3)
    }
}

public val DefaultEdgeWidth: Dp = 16.dp

private fun ContentDrawScope.drawFadedEdge(
    edgeWidth: Dp,
    leftEdge: Boolean,
) {
    val edgeWidthPx = edgeWidth.toPx()
    drawRect(
        topLeft = Offset(if (leftEdge) 0f else size.width - edgeWidthPx, 0f),
        size = Size(edgeWidthPx, size.height),
        brush = Brush.horizontalGradient(
            colors = listOf(Color.Transparent, Color.Black),
            startX = if (leftEdge) 0f else size.width,
            endX = if (leftEdge) edgeWidthPx else size.width - edgeWidthPx
        ),
        blendMode = BlendMode.DstIn
    )
}
