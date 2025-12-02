package io.github.reactivecircus.kstreamlined.android.feature.licenses.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.feature.licenses.ArtifactLicenseItem
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme

@Composable
internal fun ArtifactLicenseRow(
    item: ArtifactLicenseItem,
    modifier: Modifier = Modifier,
) {
    val uriHandler = LocalUriHandler.current
    Surface(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            item.scmUrl?.let(uriHandler::openUri)
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.title,
                    style = KSTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = item.version,
                    style = KSTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.widthIn(max = 120.dp),
                )
            }
            Text(
                text = item.description,
                style = KSTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (item.licenses.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.licenses.forEach { license ->
                        Surface(
                            color = KSTheme.colorScheme.container,
                            contentColor = KSTheme.colorScheme.onBackgroundVariant,
                            shape = CircleShape,
                        ) {
                            Text(
                                text = license,
                                style = KSTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 0.1.sp,
                                ),
                                modifier = Modifier.padding(6.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewArtifactLicenseRow() {
    KSTheme {
        Surface {
            ArtifactLicenseRow(
                item = ArtifactLicenseItem(
                    title = "Compose UI",
                    description = "androidx:compose.ui:ui",
                    version = "1.10.0",
                    scmUrl = "url",
                    licenses = listOf("Apache License 2.0"),
                ),
            )
        }
    }
}
