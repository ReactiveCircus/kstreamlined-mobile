package io.github.reactivecircus.kstreamlined.kmp.remote.mapper

import io.github.reactivecircus.kstreamlined.graphql.KotlinBlogTldrQuery
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr

internal fun KotlinBlogTldrQuery.KotlinBlogTldr.asExternalModel(): KotlinBlogTldr {
    return KotlinBlogTldr(
        id = this.id,
        content = this.content,
        model = this.model,
        generatedAt = this.generatedAt,
    )
}
