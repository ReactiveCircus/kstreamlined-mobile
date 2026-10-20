package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import io.github.reactivecircus.kstreamlined.kmp.presentation.tldrsummary.TldrSummary

internal const val SampleTitle =
    "What's new in Kotlin 2.4.20: Swift export sealed classes, Wasm compilation modes and more"

internal const val SampleGeneratedTime = "7 Sep 2026"
internal const val SampleModel = "gpt-oss-120b"

internal val SampleTldrSummary = TldrSummary(
    id = "1",
    markdownContent = """
        |Kotlin 2.4.20 is a tooling release focused on **multiplatform interop**, smaller *Wasm* binaries and a faster compiler CLI.
        |
        |## Standard library
        |
        |- Support for coroutine stack trace recovery.
        |- Experimental `allDistinct()` and `allEqual()` check collection elements.
        |- New overloads for `kotlin.test` assertion functions.
        |
        |## Multiplatform
        |
        |Swift export now maps **sealed classes** to Swift enums, so `switch` statements are exhaustive:
        |
        |```kotlin
        |sealed interface Shape {
        |    data class Circle(val radius: Double) : Shape
        |    data object Unknown : Shape
        |}
        |```
        |
        |Kotlin/Wasm adds new compilation modes and shrinks binaries by about 5–10%.
        |
        |See [What's new in Kotlin 2.4.20](https://kotlinlang.org/docs/whatsnew2420.html) for the full list.
    """.trimMargin(),
    model = SampleModel,
    displayableGeneratedTime = SampleGeneratedTime,
)
