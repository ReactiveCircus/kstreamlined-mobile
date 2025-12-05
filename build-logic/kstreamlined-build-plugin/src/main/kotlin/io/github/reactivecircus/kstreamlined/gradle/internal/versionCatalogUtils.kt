package io.github.reactivecircus.kstreamlined.gradle.internal

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.api.Project

internal val Project.libs get() = extensions.getByName("libs") as LibrariesForLibs
