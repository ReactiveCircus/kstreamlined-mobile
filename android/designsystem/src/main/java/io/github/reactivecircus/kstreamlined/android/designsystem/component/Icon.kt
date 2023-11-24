/*
 * Copyright (c) 2022 Woolworths. All rights reserved.
 */

package io.github.reactivecircus.kstreamlined.android.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.color.LocalContentColor
import androidx.compose.material3.Icon as MaterialIcon

@Composable
@NonRestartableComposable
public fun Icon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    MaterialIcon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint,
    )
}
