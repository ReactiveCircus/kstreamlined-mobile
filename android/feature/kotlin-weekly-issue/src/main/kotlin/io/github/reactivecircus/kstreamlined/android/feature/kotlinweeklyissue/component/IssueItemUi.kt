package io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Text
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.kmp.model.feed.KotlinWeeklyIssueItem

@Composable
internal fun IssueItemUi(
    item: KotlinWeeklyIssueItem,
    onItemClick: (KotlinWeeklyIssueItem) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        onClick = { onItemClick(item) },
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = 24.dp,
                vertical = 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = item.title,
                style = KSTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold
                ),
                color = KSTheme.colorScheme.primary,
            )
            Text(
                text = item.summary,
                style = KSTheme.typography.bodyMedium,
            )
            Text(
                text = item.source,
                style = KSTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                ),
                color = KSTheme.colorScheme.onBackgroundVariant,
            )
        }
    }
}

@Composable
@PreviewLightDark
private fun PreviewIssueItemUi() {
    KSTheme {
        Surface {
            IssueItemUi(
                item = KotlinWeeklyIssueItem(
                    title = "Amper Update – December 2023",
                    summary = "Last month JetBrains introduced Amper, a tool to improve the" +
                        " project configuration user experience. Marton Braun gives us an update" +
                        " about its state in December 2023.",
                    url = "https://blog.jetbrains.com/amper/2023/12/amper-update-december-2023/",
                    source = "blog.jetbrains.com",
                    group = KotlinWeeklyIssueItem.Group.Announcements,
                ),
                onItemClick = {},
            )
        }
    }
}
