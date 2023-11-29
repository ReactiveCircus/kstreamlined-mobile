package io.github.reactivecircus.kstreamlined.android.designsystem.component

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.designsystem.ThemePreviews
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons

@Composable
public fun TopNavBar(
    title: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    elevation: Dp = 2.dp,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.() -> Unit = {},
    bottomRow: @Composable (RowScope.() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        elevation = elevation,
        color = KSTheme.colorScheme.background,
        contentColor = KSTheme.colorScheme.primary,
    ) {
        Box(
            modifier = Modifier.padding(contentPadding)
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
                        text = title,
                        modifier = Modifier.weight(1f)
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
private fun GradientTitle(
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
        )
    }
}

private const val GradientHorizontalScale = 1.3f

@Composable
@ThemePreviews
private fun PreviewTopNavBar() {
    KSTheme {
        Surface {
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

@Composable
@ThemePreviews
private fun PreviewTopNavBar_withBottomRow() {
    KSTheme {
        Surface {
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
                                letterSpacing = 0.sp,
                            )
                        )
                        Icon(KSIcons.ArrowDown, contentDescription = null)
                    }
                }
            )
        }
    }
}

@Composable
@ThemePreviews
private fun PreviewTopNavBar_withNavigationIcon() {
    KSTheme {
        Surface {
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
