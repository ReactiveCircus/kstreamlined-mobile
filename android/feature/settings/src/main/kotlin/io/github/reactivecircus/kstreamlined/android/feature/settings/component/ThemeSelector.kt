package io.github.reactivecircus.kstreamlined.android.feature.settings.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings

@Composable
internal fun ThemeSelector(
    selectedTheme: AppSettings.Theme,
    onSelectTheme: (AppSettings.Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        AppSettings.Theme.entries.forEach {
            val selected = it == selectedTheme
            val borderColor = KSTheme.colorScheme.primary
            Surface(
                onClick = { onSelectTheme(it) },
                enabled = !selected,
                shape = CircleShape,
                color = KSTheme.colorScheme.container,
                contentColor = KSTheme.colorScheme.primary,
                border = if (selected) BorderStroke(2.dp, borderColor) else null,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = when (it) {
                            AppSettings.Theme.System -> KSIcons.Mobile
                            AppSettings.Theme.Light -> KSIcons.LightMode
                            AppSettings.Theme.Dark -> KSIcons.DarkMode
                        },
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .padding(vertical = 12.dp),
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = it.name.uppercase(),
                        style = KSTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.1.sp,
                        ),
                        modifier = Modifier.padding(end = 16.dp),
                    )
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewThemeSelector() {
    KSTheme {
        Surface {
            ThemeSelector(
                selectedTheme = AppSettings.Theme.System,
                onSelectTheme = {},
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
