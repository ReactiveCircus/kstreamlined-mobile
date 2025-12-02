package io.github.reactivecircus.kstreamlined.android.feature.settings.component.theme

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
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
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = KSTheme.colorScheme.container,
    ) {
        val selectionProgress by animateFloatAsState(
            targetValue = AppSettings.Theme.entries.indexOf(selectedTheme).toFloat(),
        )

        Layout(
            content = {
                AppSettings.Theme.entries.forEach {
                    ThemeOption(
                        modifier = Modifier.layoutId("themeOption"),
                        selected = it == selectedTheme,
                        theme = it,
                        onSelectTheme = onSelectTheme,
                    )
                }

                Layout(
                    modifier = Modifier
                        .layoutId("selectionIndicator")
                        .border(2.dp, KSTheme.colorScheme.primary, CircleShape),
                ) { _, constraints -> layout(constraints.minWidth, constraints.minHeight) {} }
            },
        ) { measurables, constraints ->
            val optionPlaceables = measurables
                .filter { it.layoutId == "themeOption" }
                .map { it.measure(constraints) }

            val spacing = 12.dp.roundToPx()
            val optionWidths = optionPlaceables.map { it.width }
            val offsets = optionPlaceables.runningFold(0) { sum, placeable ->
                sum + placeable.width + spacing
            }
            val layoutWidth = offsets.last() - spacing
            val layoutHeight = optionPlaceables.maxOf { it.height }

            // interpolate width and x-offset of the `selectionIndicator` based on the animated selection progress
            val leftIndex = selectionProgress.toInt()
            val rightIndex = leftIndex + 1
            val fraction = selectionProgress - leftIndex
            val currentWidth = lerp(
                start = optionWidths.getOrElse(leftIndex) { 0 },
                stop = optionWidths.getOrElse(rightIndex) { 0 },
                fraction = fraction,
            )
            val currentOffsetX = lerp(
                start = offsets.getOrElse(leftIndex) { 0 },
                stop = offsets.getOrElse(rightIndex) { 0 },
                fraction = fraction,
            )

            // measure the `selectionIndicator` with the interpolated size
            val indicatorPlaceable = measurables
                .first { it.layoutId == "selectionIndicator" }
                .measure(Constraints.fixed(width = currentWidth, height = layoutHeight))

            layout(layoutWidth, layoutHeight) {
                // place theme options
                optionPlaceables.forEachIndexed { index, placeable ->
                    placeable.place(x = offsets[index], y = 0)
                }

                // place selection indicator
                indicatorPlaceable.place(x = currentOffsetX, y = 0)
            }
        }
    }
}

@Composable
private fun ThemeOption(
    selected: Boolean,
    theme: AppSettings.Theme,
    onSelectTheme: (AppSettings.Theme) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = { onSelectTheme(theme) },
        modifier = modifier,
        enabled = !selected,
        shape = CircleShape,
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.primary,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = when (theme) {
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
                text = theme.name.uppercase(),
                style = KSTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.1.sp,
                ),
                modifier = Modifier.padding(end = 16.dp),
            )
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
