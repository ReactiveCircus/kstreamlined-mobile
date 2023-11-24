/*
 * Copyright (c) 2022 Woolworths. All rights reserved.
 */

package io.github.reactivecircus.kstreamlined.android.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LocalContentColor
import androidx.compose.material3.Surface as MaterialSurface

@Composable
@NonRestartableComposable
public fun Surface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = KSTheme.colorScheme.background,
    contentColor: Color = KSTheme.colorScheme.onBackground,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        MaterialSurface(
            modifier = modifier,
            shape = shape,
            color = color,
            contentColor = contentColor,
            tonalElevation = elevation,
            shadowElevation = elevation,
            border = border,
            content = content,
        )
    }
}

@Composable
@NonRestartableComposable
public fun Surface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RectangleShape,
    color: Color = KSTheme.colorScheme.background,
    contentColor: Color = KSTheme.colorScheme.onBackground,
    elevation: Dp = 0.dp,
    border: BorderStroke? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        MaterialSurface(
            onClick = onClick,
            modifier = modifier,
            enabled = enabled,
            shape = shape,
            color = color,
            contentColor = contentColor,
            tonalElevation = elevation,
            shadowElevation = elevation,
            border = border,
            interactionSource = interactionSource,
            content = content,
        )
    }
}
