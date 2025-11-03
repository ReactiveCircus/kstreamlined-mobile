package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.test.directives.model.SimpleDirectivesContainer

object ChameleonDirectives : SimpleDirectivesContainer() {
    val SNAPSHOT_FUNCTION by stringDirective("Snapshot function")
    val THEME_VARIANT_ENUM by stringDirective("Theme variant enum")
}
