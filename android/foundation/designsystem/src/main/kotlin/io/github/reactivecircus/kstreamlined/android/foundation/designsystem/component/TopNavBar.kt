@file:OptIn(ExperimentalSharedTransitionApi::class)

package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons

@Composable
public fun SharedTransitionScope.TopNavBar(
    title: String,
    modifier: Modifier = Modifier,
    animatedVisibilityScope: AnimatedVisibilityScope? = null,
    boundsKey: String? = null,
    titleElementKey: String? = null,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    elevation: Dp = 2.dp,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    bottomRow: @Composable (RowScope.() -> Unit)? = null,
) {
    Surface(
        modifier = modifier
            .then(
                if (boundsKey != null && animatedVisibilityScope != null) {
                    Modifier.sharedBounds(
                        sharedContentState = rememberSharedContentState(key = boundsKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 1f,
                        resizeMode = scaleToBounds(ContentScale.FillWidth, Alignment.TopCenter),
                    )
                } else {
                    Modifier
                },
            ),
        elevation = elevation,
        color = KSTheme.colorScheme.background,
        contentColor = KSTheme.colorScheme.primary,
    ) {
        Box(
            modifier = Modifier.padding(contentPadding),
        ) {
            Column(
                modifier = Modifier.padding(vertical = 16.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (navigationIcon != null) {
                        Spacer(modifier = Modifier.width(16.dp))
                        navigationIcon()
                        Spacer(modifier = Modifier.width(8.dp))
                    } else {
                        Spacer(modifier = Modifier.width(24.dp))
                    }

                    GradientTitle(
                        animatedVisibilityScope = animatedVisibilityScope,
                        titleElementKey = titleElementKey,
                        text = title,
                        modifier = Modifier.weight(1f),
                    )

                    actions()

                    Spacer(modifier = Modifier.width(16.dp))
                }

                if (bottomRow != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp),
                        content = bottomRow,
                    )
                }
            }
        }
    }
}

@Composable
private fun SharedTransitionScope.GradientTitle(
    animatedVisibilityScope: AnimatedVisibilityScope?,
    titleElementKey: String?,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        val gradient = KSTheme.colorScheme.gradient
        val brush = remember {
            object : ShaderBrush() {
                override fun createShader(size: Size): Shader {
                    return LinearGradientShader(
                        colors = gradient,
                        from = Offset(0f, size.height),
                        to = Offset(size.width * GradientHorizontalScale, 0f),
                    )
                }
            }
        }
        Text(
            text = text,
            style = KSTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                brush = brush,
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = if (titleElementKey != null && animatedVisibilityScope != null) {
                Modifier
                    .sharedElement(
                        sharedContentState = rememberSharedContentState(key = titleElementKey),
                        animatedVisibilityScope = animatedVisibilityScope,
                        zIndexInOverlay = 1f,
                    )
                    .skipToLookaheadSize()
            } else {
                Modifier
            },
        )
    }
}

private const val GradientHorizontalScale = 1.3f

@Composable
@PreviewLightDark
private fun PreviewTopNavBar() {
    KSTheme {
        Surface {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    actions = {
                        FilledIconButton(
                            KSIcons.Settings,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewTopNavBar_withBottomRow() {
    KSTheme {
        Surface {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    actions = {
                        FilledIconButton(
                            KSIcons.Settings,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                    bottomRow = {
                        Chip(
                            onClick = {},
                            contentColor = KSTheme.colorScheme.primary,
                        ) {
                            Text(
                                text = "Button".uppercase(),
                                style = KSTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.1.sp,
                                ),
                            )
                            Icon(KSIcons.ArrowDown, contentDescription = null)
                        }
                    },
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewTopNavBar_withNavigationIcon() {
    KSTheme {
        Surface {
            SharedTransitionLayout {
                TopNavBar(
                    title = "Title",
                    navigationIcon = {
                        LargeIconButton(
                            KSIcons.Close,
                            contentDescription = null,
                            onClick = {},
                        )
                    },
                )
            }
        }
    }
}
