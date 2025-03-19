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
import kotlinx.datetime.Instant
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalSharedTransitionApi::class)
@RunWith(TestParameterInjector::class)
class KotlinBlogCardTest {

    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_KotlinBlogCard(
        @TestParameter saved: Boolean,
        @TestParameter themeVariant: ThemeVariant,
    ) {
        snapshotTester.snapshot(themeVariant = themeVariant) {
            SharedTransitionLayout {
                KotlinBlogCard(
                    item = FeedItem.KotlinBlog(
                        id = "1",
                        title = "Kotlin Multiplatform Development Roadmap for 2024",
                        publishTime = Instant.parse("2023-11-16T11:59:46Z"),
                        contentUrl = "contentUrl",
                        savedForLater = saved,
                        featuredImageUrl = "",
                    ).toDisplayable("Moments ago"),
                    onItemClick = {},
                    onSaveButtonClick = {},
                    modifier = Modifier.padding(24.dp),
                )
            }
        }
    }
}
