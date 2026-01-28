package io.github.reactivecircus.routebinding.compiler

import org.jetbrains.kotlin.name.ClassId

internal object ClassIds {
    object RouteBinding {
        val Annotation = ClassId.fromString("io/github/reactivecircus/routebinding/runtime/RouteBinding")
        val NavEntryInstaller = ClassId.fromString("io/github/reactivecircus/routebinding/runtime/NavEntryInstaller")
    }

    object Nav3 {
        val EntryProviderScope = ClassId.fromString("androidx/navigation3/runtime/EntryProviderScope")
    }

    object Metro {
        val AppScope = ClassId.fromString("dev/zacsweers/metro/AppScope")
        val ContributesIntoSet = ClassId.fromString("dev/zacsweers/metro/ContributesIntoSet")
        val Factory = ClassId.fromString("dev/zacsweers/metro/internal/Factory")
    }
}
