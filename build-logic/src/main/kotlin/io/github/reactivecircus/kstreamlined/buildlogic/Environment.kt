package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project
import org.gradle.api.provider.Provider

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

val Project.isIdeBuild: Boolean
    get() = providers.systemProperty("idea.active").orNull == "true"

val Project.isAppleSilicon: Boolean
    get() = providers.systemProperty("os.arch").orNull == "aarch64"

fun Project.envOrProp(name: String): Provider<String> {
    return providers.environmentVariable(name).orElse(
        providers.gradleProperty(name).orElse("")
    )
}
