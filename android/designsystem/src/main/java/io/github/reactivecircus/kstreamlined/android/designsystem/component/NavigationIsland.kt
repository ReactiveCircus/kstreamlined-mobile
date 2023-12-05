package io.github.reactivecircus.kstreamlined.android.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Bookmarks
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon.Kotlin

@Composable
public fun NavigationIsland(
    modifier: Modifier = Modifier,
    items: @Composable RowScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = KSTheme.colorScheme.containerInverse,
        contentColor = KSTheme.colorScheme.onContainerInverse,
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            items()
        }
    }
}

@Composable
public fun NavigationIslandItem(
    selected: Boolean,
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .minimumInteractiveComponentSize()
            .size(72.dp)
            .clickable(
                onClick = onClick,
                role = Role.Button,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = false,
                    radius = 40.dp,
                ),
            ),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = if (selected) {
                KSTheme.colorScheme.primary
            } else {
                KSTheme.colorScheme.onContainerInverse
            },
        )
    }
}

@Composable
public fun NavigationIslandDivider(
    modifier: Modifier = Modifier,
) {
    VerticalDivider(
        modifier = modifier.height(16.dp),
        color = KSTheme.colorScheme.onContainerInverse,
    )
}

@Composable
@PreviewLightDark
private fun PreviewNavigationIsland() {
    KSTheme {
        Surface {
            NavigationIsland(
                modifier = Modifier.padding(8.dp),
            ) {
                NavigationIslandItem(
                    selected = true,
                    icon = KSIcons.Kotlin,
                    contentDescription = "Home",
                    onClick = {},
                )
                NavigationIslandDivider()
                NavigationIslandItem(
                    selected = false,
                    icon = KSIcons.Bookmarks,
                    contentDescription = "Saved",
                    onClick = {},
                )
            }
        }
    }
}
