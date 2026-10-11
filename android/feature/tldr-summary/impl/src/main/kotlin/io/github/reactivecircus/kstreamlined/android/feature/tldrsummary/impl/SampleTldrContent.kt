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
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

internal const val SampleTitle = "What's new in Kotlin 2.3.0: name-based destructuring, guard conditions and more"
internal const val SampleGeneratedTime = "2 Oct 2025"
internal const val SampleModel = "gpt-oss-120b"

@Composable
internal fun rememberSampleTldrContent(): AnnotatedString {
    val headingStyle = KSTheme.typography.titleMedium.toSpanStyle()
    val codeStyle = SpanStyle(background = KSTheme.colorScheme.surface)
    val linkStyle = SpanStyle(
        color = KSTheme.colorScheme.accent,
        fontWeight = FontWeight.Bold,
        textDecoration = TextDecoration.Underline,
    )
    return remember(headingStyle, codeStyle, linkStyle) {
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
                withStyle(codeStyle) { append(" $text ") }
            }

            fun bold(text: String) {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(text) }
            }

            paragraph {
                append(
                    "Kotlin 2.3.0 is a language and tooling release focused on safer, " +
                        "more expressive code and faster builds.",
                )
            }
            spacer()
            heading("Language")
            bullet {
                bold("Name-based destructuring")
                append(" matches properties by name rather than position.")
            }
            bullet {
                bold("Guard conditions")
                append(" in ")
                code("when")
                append(" branches are now stable.")
            }
            bullet {
                append("Smarter smart casts across lambdas and ")
                code("try/catch")
                append(".")
            }
            spacer()
            heading("Multiplatform")
            bullet { append("Swift export is enabled by default for new projects.") }
            bullet { append("Kotlin/Wasm produces smaller binaries.") }
            spacer()
            heading("Tooling")
            bullet { append("Faster incremental builds with the K2 compiler.") }
            bullet {
                append("See the ")
                withLink(LinkAnnotation.Url("https://kotlinlang.org", TextLinkStyles(linkStyle))) {
                    append("migration guide")
                }
                append(" for build changes.")
            }
            spacer()
            paragraph {
                append("Update via the Kotlin Gradle plugin or IntelliJ IDEA's bundled plugin.")
            }
        }
    }
}
