package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem

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
        modifier = modifier
            .drawBehind {
                drawRect(brush)
            }
            .fillMaxWidth(),
        color = Color.Transparent,
    ) {
        IssueGroupBadge(
            group = group,
            modifier = Modifier.padding(
                top = 24.dp,
                bottom = 8.dp,
                start = 24.dp,
                end = 24.dp,
            )
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
        color = when (group) {
            KotlinWeeklyIssueItem.Group.Announcements -> AnnouncementsColor
            KotlinWeeklyIssueItem.Group.Articles -> ArticlesColor
            KotlinWeeklyIssueItem.Group.Android -> AndroidColor
            KotlinWeeklyIssueItem.Group.Videos -> VideosColor
            KotlinWeeklyIssueItem.Group.Libraries -> LibrariesColor
        },
        contentColor = Color.White,
    ) {
        Text(
            text = group.name.uppercase(),
            style = KSTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.sp,
            ),
            modifier = Modifier.padding(8.dp),
        )
    }
}

private val AnnouncementsColor = Color(0xFF7874B4)
private val ArticlesColor = Color(0xFFF1646C)
private val AndroidColor = Color(0xFF79C5B4)
private val VideosColor = Color(0xFF639FCB)
private val LibrariesColor = Color(0xFF800000)

@Composable
@PreviewLightDark
private fun PreviewIssueGroupBadge(
    @PreviewParameter(IssueGroupProvider::class) issueGroup: KotlinWeeklyIssueItem.Group
) {
    KSTheme {
        Surface {
            IssueGroupBadge(
                group = issueGroup,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }
}

private class IssueGroupProvider : PreviewParameterProvider<KotlinWeeklyIssueItem.Group> {
    override val values: Sequence<KotlinWeeklyIssueItem.Group> =
        KotlinWeeklyIssueItem.Group.entries.asSequence()
}
