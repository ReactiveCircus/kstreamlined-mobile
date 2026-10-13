package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Icon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl.R

@Composable
internal fun TldrBadge(modifier: Modifier = Modifier) {
    val brandGradient = KSTheme.colorScheme.brandGradient
    Row(
        modifier = modifier
            .background(
                brush = Brush.horizontalGradient(brandGradient.take(2)),
                shape = CircleShape,
            )
            .padding(start = 8.dp, end = 10.dp)
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            KSIcons.Sparkles,
            contentDescription = null,
            modifier = Modifier.requiredSize(20.dp),
            tint = KSTheme.colorScheme.onAccent,
        )
        Text(
            text = stringResource(R.string.title_tldr_summary),
            style = KSTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.1.sp,
            ),
            color = KSTheme.colorScheme.onAccent,
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewTldrBadge() {
    TldrBadge(modifier = Modifier.padding(16.dp))
}
