package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import io.github.reactivecircus.kstreamlined.kmp.prettytime.toFormattedTime
import io.github.reactivecircus.kstreamlined.kmp.remote.model.KotlinBlogTldr
import kotlinx.datetime.TimeZone

context(timeZone: TimeZone)
internal fun KotlinBlogTldr.asPresentationModel(): TldrSummary {
    return TldrSummary(
        id = id,
        markdownContent = content,
        model = model,
        displayableGeneratedTime = generatedAt.toFormattedTime(),
    )
}
