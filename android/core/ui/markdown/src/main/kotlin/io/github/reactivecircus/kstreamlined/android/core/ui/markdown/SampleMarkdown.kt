package io.github.reactivecircus.kstreamlined.android.core.ui.markdown

internal object SampleMarkdown {
    val Tldr = """
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
    """.trimMargin()

    val Headings = """
        |# Heading 1
        |
        |Paragraph after heading 1.
        |
        |## Heading 2 with `code`
        |
        |Paragraph after heading 2.
        |
        |### Heading 3
        |
        |Paragraph after heading 3.
        |
        |#### Heading 4
        |
        |Paragraph after heading 4.
    """.trimMargin()

    val Inlines = """
        |Text with **bold**, *italic*, ***bold italic*** and `inline code`.
        |
        |Inline code next to punctuation: `listOf()`, `mapOf()`. And (`parentheses`).
        |
        |A long inline code span that wraps: `kotlinx.coroutines.flow.MutableStateFlow.compareAndSet(expect, update)` in the middle of a sentence.
        |
        |Links: [kotlinlang.org](https://kotlinlang.org), [a link with `code` and **bold**](https://kotlinlang.org/docs) and <https://github.com/JetBrains/kotlin>.
        |
        |Hard line break after this line  
        |and a soft line break
        |after this one.
    """.trimMargin()

    val Lists = """
        |Bullet list:
        |
        |- First item
        |- Second item with a long line that wraps onto the next line to check the hanging indent.
        |  - Nested item
        |    - Deeply nested item with `code`
        |- Third item
        |
        |Ordered list starting at 9:
        |
        |9. Ninth
        |10. Tenth with a long line that wraps onto the next line to check the hanging indent.
        |11. Eleventh
        |
        |Loose list:
        |
        |1. First paragraph.
        |
        |   Second paragraph of the first item.
        |
        |2. Item with code:
        |
        |   ```kotlin
        |   val answer = 42
        |   ```
    """.trimMargin()

    val CodeBlocks = """
        |Code block with a language:
        |
        |```kotlin
        |fun main() {
        |    val greeting = "Hello, KStreamlined! This line is long enough to scroll horizontally."
        |    println(greeting)
        |}
        |```
        |
        |Code block without a language:
        |
        |```
        |./gradlew :app:assembleRelease
        |```
    """.trimMargin()

    val Others = """
        |> A block quote with *emphasis* and `code`
        |> that spans multiple lines.
        |>
        |> - with a list
        |
        |---
        |
        |Fallbacks: ![image alt text](https://example.com/image.png), <span>raw html</span>, ~~strikethrough~~ and a bare URL https://kotlinlang.org.
        |
        || Table | Header |
        || ----- | ------ |
        || a     | b      |
    """.trimMargin()
}
