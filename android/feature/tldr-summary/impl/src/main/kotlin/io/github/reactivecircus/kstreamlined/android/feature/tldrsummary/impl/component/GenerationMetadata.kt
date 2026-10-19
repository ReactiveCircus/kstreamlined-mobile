package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.R

@Composable
internal fun GenerationMetadata(
    displayableGeneratedTime: String,
    model: String,
    modifier: Modifier = Modifier,
) {
    val textStyle = KSTheme.typography.labelMedium
    val textColor = KSTheme.colorScheme.onBackgroundMuted
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.tldr_generated_by),
            style = textStyle,
            color = textColor,
        )
        Surface(
            shape = CircleShape,
            color = KSTheme.colorScheme.surfaceAccent,
            contentColor = KSTheme.colorScheme.onSurfaceAccent,
        ) {
            Text(
                text = model,
                style = textStyle.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.1.sp,
                ),
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            )
        }
        Text(
            text = stringResource(R.string.tldr_generated_on, displayableGeneratedTime),
            style = textStyle,
            color = textColor,
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewGenerationMetadata() {
    GenerationMetadata(
        displayableGeneratedTime = "7 Sep 2026",
        model = "gpt-oss-120b",
        modifier = Modifier.padding(24.dp),
    )
}
