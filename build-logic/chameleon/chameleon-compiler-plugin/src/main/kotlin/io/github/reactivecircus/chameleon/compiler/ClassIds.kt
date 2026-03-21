package io.github.reactivecircus.chameleon.compiler

import org.jetbrains.kotlin.name.ClassId

internal object ClassIds {
    object Chameleon {
        val Annotation = ClassId.fromString("io/github/reactivecircus/chameleon/runtime/Chameleon")
    }

    object Burst {
        val Annotation = ClassId.fromString("app/cash/burst/Burst")
    }
}
