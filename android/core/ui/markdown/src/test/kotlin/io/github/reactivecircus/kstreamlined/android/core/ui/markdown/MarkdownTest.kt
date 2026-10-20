package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class MarkdownTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Markdown_Tldr() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.Tldr, modifier = Modifier.padding(24.dp))
        }
    }

    @Test
    fun snapshot_Markdown_Headings() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.Headings, modifier = Modifier.padding(24.dp))
        }
    }

    @Test
    fun snapshot_Markdown_Inlines() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.Inlines, modifier = Modifier.padding(24.dp))
        }
    }

    @Test
    fun snapshot_Markdown_Lists() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.Lists, modifier = Modifier.padding(24.dp))
        }
    }

    @Test
    fun snapshot_Markdown_CodeBlocks() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.CodeBlocks, modifier = Modifier.padding(24.dp))
        }
    }

    @Test
    fun snapshot_Markdown_Others() {
        snapshotTester.snapshot {
            Markdown(SampleMarkdown.Others, modifier = Modifier.padding(24.dp))
        }
    }
}
