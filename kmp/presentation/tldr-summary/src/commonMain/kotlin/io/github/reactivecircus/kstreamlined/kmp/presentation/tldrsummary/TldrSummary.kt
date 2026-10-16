package io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary

import androidx.compose.runtime.Immutable

@Immutable
public data class TldrSummary(
    val id: String,
    val markdownContent: String,
    val model: String,
    val displayableGeneratedTime: String,
)
