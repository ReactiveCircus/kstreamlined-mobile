package io.github.reactivecircus.kstreamlined.android.foundation.commonui.feed

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.testing.junit.testparameterinjector.TestParameter
import com.google.testing.junit.testparameterinjector.TestParameterInjector
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.ThemeVariant
import io.github.reactivecircus.kstreamlined.kmp.model.feed.FeedItem
import io.github.reactivecircus.kstreamlined.kmp.model.feed.toDisplayable
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.time.Instant

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(TestParameterInjector::class)
class TalkingKotlinCardTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_TalkingKotlinCard(
        @TestParameter saved: Boolean,
        @TestParameter themeVariant: ThemeVariant,
    ) {
        snapshotTester.snapshot(themeVariant = themeVariant) {
            SharedTransitionLayout {
                TalkingKotlinCard(
                    item = FeedItem.TalkingKotlin(
                        id = "1",
                        title = "Making Multiplatform Better",
                        publishTime = Instant.parse("2023-09-18T22:00:00Z"),
                        contentUrl = "contentUrl",
                        savedForLater = saved,
                        audioUrl = "",
                        thumbnailUrl = "",
                        summary = "In this episode, we talk to Rick Clephas.",
                        summaryFormat = FeedItem.TalkingKotlin.ContentFormat.Text,
                        summaryPlainText = null,
                        duration = "1h 3min.",
                        startPositionMillis = 0,
                    ).toDisplayable("Moments ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
