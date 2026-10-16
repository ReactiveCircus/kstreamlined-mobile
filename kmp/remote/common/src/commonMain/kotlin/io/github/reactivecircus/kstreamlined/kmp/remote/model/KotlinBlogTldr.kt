package io.github.reactivecircus.kstreamlined.kmp.remote.model

import kotlin.time.Instant

public data class KotlinBlogTldr(
    val id: String,
    val content: String,
    val model: String,
    val generatedAt: Instant,
)
