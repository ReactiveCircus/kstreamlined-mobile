package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.test.directives.model.SimpleDirectivesContainer

object RouteBindingDirectives : SimpleDirectivesContainer() {
    val ENABLE_COMPOSE_COMPILER by directive("Enable Compose Compiler.")
}
