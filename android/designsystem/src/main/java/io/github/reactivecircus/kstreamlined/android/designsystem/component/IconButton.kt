/*
 * Copyright (c) 2022 Woolworths. All rights reserved.
 */

package io.github.reactivecircus.kstreamlined.android.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LocalContentColor
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.BookmarkAdd
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons

@Composable
@NonRestartableComposable
public fun IconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = LocalContentColor.current,
) {
    IconButtonImpl(
        imageVector = imageVector,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconTint = iconTint,
        containerSize = DefaultContainerSize,
        iconSize = DefaultIconSize,
    )
}

@Composable
@NonRestartableComposable
public fun LargeIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = LocalContentColor.current,
) {
    IconButtonImpl(
        imageVector = imageVector,
        contentDescription = contentDescription,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        iconTint = iconTint,
        containerSize = LargeContainerSize,
        iconSize = LargeIconSize,
    )
}

@Composable
private fun IconButtonImpl(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    iconTint: Color,
    containerSize: Dp,
    iconSize: Dp,
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(containerSize)
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                enabled = enabled,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = containerSize / 2
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            modifier = Modifier.requiredSize(iconSize),
            tint = iconTint,
        )
    }
}

@Composable
public fun FilledIconButton(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    containerColor: Color = KSTheme.colorScheme.container,
    iconTint: Color = LocalContentColor.current,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = enabled,
        shape = CircleShape,
        color = containerColor,
        contentColor = iconTint,
    ) {
        Box(
            modifier = Modifier.size(DefaultContainerSize),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = Modifier.requiredSize(DefaultIconSize),
                tint = iconTint,
            )
        }
    }
}

private val DefaultIconSize = 24.dp
private val DefaultContainerSize = 40.dp

private val LargeIconSize = 32.dp
private val LargeContainerSize = 48.dp

@Composable
@PreviewLightDark
private fun PreviewIconButton() {
    KSTheme {
        Surface {
            IconButton(
                KSIcons.BookmarkAdd,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewLargeIconButton() {
    KSTheme {
        Surface {
            LargeIconButton(
                KSIcons.Close,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewFilledIconButton() {
    KSTheme {
        Surface {
            FilledIconButton(
                KSIcons.Settings,
                contentDescription = null,
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
