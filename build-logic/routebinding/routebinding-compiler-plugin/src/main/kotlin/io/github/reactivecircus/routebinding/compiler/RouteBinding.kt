@file:Suppress("PropertyName")

package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.name.ClassId

internal object RouteBinding {
    const val PLUGIN_ID = "io.github.reactivecircus.routebinding.compiler"
    val ANNOTATION_ID = ClassId.fromString("io/github/reactivecircus/routebinding/runtime/RouteBinding")
}
