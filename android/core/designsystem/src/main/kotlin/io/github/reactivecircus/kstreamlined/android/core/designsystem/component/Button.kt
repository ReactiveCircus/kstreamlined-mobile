package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

@Composable
public fun Button(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = KSTheme.colorScheme.container,
    contentColor: Color = KSTheme.colorScheme.primary,
) {
    Surface(
        onClick = onClick,
        modifier = modifier.semantics { role = Role.Button },
        enabled = enabled,
        shape = CircleShape,
        color = color,
        contentColor = contentColor,
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = 12.dp,
                horizontal = 16.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = text.uppercase(),
                style = KSTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                ),
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewButton() {
    KSTheme {
        Surface {
            Button(
                text = "Button",
                onClick = {},
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}
