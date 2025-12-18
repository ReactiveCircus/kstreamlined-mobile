package io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

@Composable
@PreviewLightDark
private fun PreviewKSIcons() {
    KSTheme {
        Surface {
            FlowRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 4,
            ) {
                KSIcons.asList().forEach {
                    Icon(
                        painter = it,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}
