package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.tracing.trace
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser.parseMarkdown

@Composable
public fun Markdown(
    markdown: String,
    modifier: Modifier = Modifier,
) {
    val document = remember(markdown) {
        trace("Markdown:parse") { parseMarkdown(markdown) }
    }
    MarkdownBlocks(
        blocks = document.blocks,
        style = rememberDefaultMarkdownStyle(),
        modifier = modifier,
    )
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_Tldr() {
    Markdown(SampleMarkdown.Tldr, modifier = Modifier.padding(24.dp))
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_Headings() {
    Markdown(SampleMarkdown.Headings, modifier = Modifier.padding(24.dp))
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_Inlines() {
    Markdown(SampleMarkdown.Inlines, modifier = Modifier.padding(24.dp))
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_Lists() {
    Markdown(SampleMarkdown.Lists, modifier = Modifier.padding(24.dp))
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_CodeBlocks() {
    Markdown(SampleMarkdown.CodeBlocks, modifier = Modifier.padding(24.dp))
}

@Composable
@PreviewKStreamlined
private fun PreviewMarkdown_Others() {
    Markdown(SampleMarkdown.Others, modifier = Modifier.padding(24.dp))
}
