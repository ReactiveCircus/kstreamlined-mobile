@file:Suppress("PropertyName")

package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.name.ClassId

internal object Chameleon {
    const val PLUGIN_ID = "io.github.reactivecircus.chameleon.compiler"
    val ANNOTATION_ID = ClassId.fromString("io/github/reactivecircus/chameleon/runtime/Chameleon")
}
