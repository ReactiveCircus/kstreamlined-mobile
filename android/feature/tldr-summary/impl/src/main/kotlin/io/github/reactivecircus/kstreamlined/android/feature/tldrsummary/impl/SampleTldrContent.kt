package io.github.reactivecircus.kstreamlined.android.feature.tldrsummary.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

internal const val SampleTitle =
    "What's new in Kotlin 2.4.20: Swift export sealed classes, Wasm compilation modes and more"
internal const val SampleGeneratedTime = "7 Sep 2026"
internal const val SampleModel = "gpt-oss-120b"

internal const val InlineCodeTag = "inline-code"

// Narrow no-break space (0.3em wide at this size) reserving room for the background's horizontal
// padding, without allowing a line break between the code and its padding.
private const val InlineCodeSpacing = '\u202F'
private val InlineCodeSpacingStyle = SpanStyle(fontSize = 0.5.em)

@Composable
internal fun rememberSampleTldrContent(): AnnotatedString {
    val headingStyle = KSTheme.typography.titleMedium.toSpanStyle()
    val linkStyle = SpanStyle(
        color = KSTheme.colorScheme.accent,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
    )
    return remember(headingStyle, linkStyle) {
        buildAnnotatedString {
            fun paragraph(block: AnnotatedString.Builder.() -> Unit) {
                withStyle(ParagraphStyle(lineHeight = 1.5.em)) { block() }
            }

            fun spacer() {
                withStyle(ParagraphStyle(lineHeight = 0.75.em)) { append(" ") }
            }

            fun heading(text: String) {
                withStyle(ParagraphStyle()) {
                    withStyle(headingStyle) { append(text) }
                }
            }

            fun bullet(block: AnnotatedString.Builder.() -> Unit) {
                withStyle(
                    ParagraphStyle(
                        lineHeight = 1.5.em,
                        textIndent = TextIndent(firstLine = 0.sp, restLine = 16.sp),
                    ),
                ) {
                    append("•  ")
                    block()
                }
            }

            fun code(text: String) {
                withStyle(InlineCodeSpacingStyle) { append(InlineCodeSpacing) }
                withAnnotation(tag = InlineCodeTag, annotation = text) { append(text) }
                withStyle(InlineCodeSpacingStyle) { append(InlineCodeSpacing) }
            }

            fun bold(text: String) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text) }
            }

            paragraph {
                append(
                    "Kotlin 2.4.20 is a tooling release focused on multiplatform interop, " +
                        "smaller Wasm binaries and a faster compiler CLI.",
                )
            }
            spacer()
            heading("Standard library")
            bullet { append("Support for coroutine stack trace recovery.") }
            bullet {
                append("Experimental ")
                code("allDistinct()")
                append(" and ")
                code("allEqual()")
                append(" check collection elements.")
            }
            bullet {
                append("New overloads for ")
                code("kotlin.test")
                append(" assertion functions.")
            }
            spacer()
            heading("Multiplatform")
            bullet {
                append("Swift export maps ")
                bold("sealed classes")
                append(" to Swift enums for exhaustive ")
                code("switch")
                append(".")
            }
            bullet {
                append("Generated ")
                code("Package.swift")
                append(" for SwiftPM dependencies.")
            }
            bullet {
                append("Kotlin/Wasm adds new ")
                bold("compilation modes")
                append(" and shrinks binaries by about 5–10%.")
            }
            spacer()
            heading("Tooling")
            bullet {
                append("The Kotlin runner command is now ")
                code("kotlinr")
                append(".")
            }
            bullet {
                append("Experimental compiler ")
                bold("native image")
                append(" for faster startup.")
            }
            bullet {
                append("See ")
                withLink(
                    LinkAnnotation.Url(
                        url = "https://kotlinlang.org/docs/whatsnew2420.html",
                        styles = TextLinkStyles(linkStyle),
                    ),
                ) {
                    append("What's new in Kotlin 2.4.20")
                }
                append(" for the full list.")
            }
            spacer()
            paragraph {
                append("Update your IDE, then change the Kotlin version to 2.4.20 in your build scripts.")
            }
        }
    }
}
