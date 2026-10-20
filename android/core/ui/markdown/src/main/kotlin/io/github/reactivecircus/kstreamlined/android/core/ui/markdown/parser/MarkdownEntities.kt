package io.github.reactivecircus.kstreamlined.android.core.ui.markdown.parser

import org.intellij.markdown.html.entities.Entities

/**
 * Decodes CommonMark entity / numeric character references and (optionally) backslash escapes into plain text.
 */
internal fun decodeEntitiesAndEscapes(text: CharSequence, processEscapes: Boolean = true): String {
    return EntityOrEscapeRegex.replace(text) { match ->
        val groups = match.groups
        groups[EscapedCharGroup]?.let { return@replace if (processEscapes) it.value else match.value }
        groups[HexCodePointGroup]?.let { return@replace codePointToString(it.value.toInt(radix = 16)) }
        groups[DecimalCodePointGroup]?.let { return@replace codePointToString(it.value.toInt()) }
        Entities.map[match.value]?.let(::codePointToString) ?: match.value
    }
}

private fun codePointToString(codePoint: Int): String {
    val isValid = codePoint in 1..Character.MAX_CODE_POINT &&
        codePoint !in Character.MIN_SURROGATE.code..Character.MAX_SURROGATE.code
    return if (isValid) String(Character.toChars(codePoint)) else ReplacementCharacter
}

private const val EntityPattern = """&(?:#[xX]([0-9a-fA-F]{1,6})|#([0-9]{1,7})|[a-zA-Z][a-zA-Z0-9]{0,31});"""
private const val EscapePattern = """\\([!-/:-@\[-`{-~])"""

private val EntityOrEscapeRegex = Regex("$EntityPattern|$EscapePattern")

private const val HexCodePointGroup = 1
private const val DecimalCodePointGroup = 2
private const val EscapedCharGroup = 3
private const val ReplacementCharacter = "\uFFFD"
