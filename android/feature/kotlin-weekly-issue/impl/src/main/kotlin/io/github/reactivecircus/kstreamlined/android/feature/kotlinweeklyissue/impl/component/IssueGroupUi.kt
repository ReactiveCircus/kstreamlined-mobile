package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.ExperimentalFoundationStyleApi
import androidx.compose.foundation.style.fillWidth
import androidx.compose.foundation.style.styleable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.kmp.feed.model.KotlinWeeklyIssueItem

@OptIn(ExperimentalFoundationStyleApi::class)
@Composable
internal fun IssueGroupUi(
    group: KotlinWeeklyIssueItem.Group,
    modifier: Modifier = Modifier,
) {
    val brush = Brush.verticalGradient(
        colors = listOf(
            KSTheme.colorScheme.background,
            Color.Transparent,
        ),
    )
    Surface(
        modifier = modifier.styleable {
            background(brush)
            fillWidth()
        },
        color = Color.Transparent,
    ) {
        IssueGroupBadge(
            group = group,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 8.dp,
                start = 24.dp,
                end = 24.dp,
            ),
        )
    }
}

@Composable
private fun IssueGroupBadge(
    group: KotlinWeeklyIssueItem.Group,
    modifier: Modifier,
) {
    Surface(
        modifier = modifier.wrapContentWidth(Alignment.Start),
        shape = RoundedCornerShape(4.dp),
        color = Color(group.sourceColorArgb.toInt()),
        contentColor = Color(group.onSourceColorArgb.toInt()),
    ) {
        Text(
            text = group.name.uppercase(),
            style = KSTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
            ),
            modifier = Modifier.padding(8.dp),
        )
    }
}

@Composable
@PreviewKStreamlined
private fun PreviewIssueGroupBadge(
    @PreviewParameter(IssueGroupProvider::class) issueGroup: KotlinWeeklyIssueItem.Group,
) {
    IssueGroupBadge(
        group = issueGroup,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
    )
}

private class IssueGroupProvider : PreviewParameterProvider<KotlinWeeklyIssueItem.Group> {
    override val values: Sequence<KotlinWeeklyIssueItem.Group> =
        KotlinWeeklyIssueItem.Group.entries.asSequence()
}
