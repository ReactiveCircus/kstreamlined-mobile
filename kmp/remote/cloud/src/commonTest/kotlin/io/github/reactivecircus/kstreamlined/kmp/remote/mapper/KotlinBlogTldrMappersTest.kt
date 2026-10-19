package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinBlogTldrQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Instant

class KotlinBlogTldrMappersTest {
    @Test
    fun `KotlinBlogTldrQuery_KotlinBlogTldr maps to expected KotlinBlogTldr`() {
        val tldr = KotlinBlogTldrQuery.KotlinBlogTldr(
            __typename = "KotlinBlogTldr",
            id = "1",
            content = "TLDR content",
            model = "gpt-oss-120b",
            generatedAt = Instant.parse("2026-09-07T00:00:00Z"),
        )
        val expected = KotlinBlogTldr(
            id = "1",
            content = "TLDR content",
            model = "gpt-oss-120b",
            generatedAt = Instant.parse("2026-09-07T00:00:00Z"),
        )
        assertEquals(expected, tldr.asExternalModel())
    }
}
