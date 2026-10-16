package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import kotlinx.datetime.TimeZone
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class TldrSummaryMapperTest {
    @Test
    fun `transformed TldrSummary has expected content, model and displayable generated time`() {
        val timeZone = TimeZone.UTC

        val tldr = KotlinBlogTldr(
            id = "1",
            content = "### Summary\n- Point 1",
            model = "gpt-oss-120b",
            generatedAt = Instant.parse("2026-09-07T03:10:54Z"),
        )

        val expectedTldrSummary = TldrSummary(
            id = "1",
            markdownContent = "### Summary\n- Point 1",
            model = "gpt-oss-120b",
            displayableGeneratedTime = "7 Sep 2026",
        )

        assertEquals(expectedTldrSummary, tldr.asPresentationModel(timeZone = timeZone))
    }
}
