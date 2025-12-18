package io.github.reactivecircus.kstreamlined.android.feature.settings.component.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.feature.settings.R

@Composable
internal fun SourceCodeTile(
    sourceCodeUrl: String,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Surface(
        onClick = {
            uriHandler.openUri(sourceCodeUrl)
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KSTheme.colorScheme.container,
        contentColor = KSTheme.colorScheme.onBackgroundVariant,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = KSIcons.Code,
                contentDescription = null,
                modifier = Modifier.padding(start = 12.dp),
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
                    .padding(vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = stringResource(R.string.setting_source_code_title),
                    style = KSTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )

                Text(
                    text = stringResource(R.string.setting_source_code_description),
                    style = KSTheme.typography.labelMedium,
                )
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewSourceCodeTile() {
    KSTheme {
        Surface {
            SourceCodeTile(
                sourceCodeUrl = "",
                modifier = Modifier.padding(24.dp),
            )
        }
    }
}
