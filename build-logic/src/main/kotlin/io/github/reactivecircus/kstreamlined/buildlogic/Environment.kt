package io.github.reactivecircus.kstreamlined.buildlogic

import org.gradle.api.Project

val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

val Project.isIdeBuild: Boolean
    get() = providers.systemProperty("idea.active").orNull == "true"

val Project.isRunningBenchmark: Boolean
    get() {
        val benchmarkPackageName = "io.github.reactivecircus.kstreamlined.android.benchmark"
        return providers.gradleProperty("android.testInstrumentationRunnerArguments.class")
            .orNull?.startsWith(benchmarkPackageName) == true
    }

val Project.isAppleSilicon: Boolean
    get() = providers.systemProperty("os.arch").orNull == "aarch64"

fun Project.envOrProp(name: String): String {
    return providers.environmentVariable(name).orNull
        ?: providers.gradleProperty(name).getOrElse("")
}
