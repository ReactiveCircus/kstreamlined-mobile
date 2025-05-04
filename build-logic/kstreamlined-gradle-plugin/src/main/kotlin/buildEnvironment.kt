import org.gradle.api.Project
import org.gradle.api.provider.Provider

public val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

public val Project.isIdeBuild: Boolean
    get() = providers.systemProperty("idea.active").orNull == "true"

public fun Project.envOrProp(name: String): Provider<String> {
    return providers.environmentVariable(name).orElse(
        providers.gradleProperty(name).orElse("")
    )
}

internal val Project.runningPaparazzi: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.substringAfterLast(":").contains("paparazzi", ignoreCase = true)
    }

internal val Project.runningCheck: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.substringAfterLast(":").equals("check", ignoreCase = true)
    }

internal val Project.isGeneratingBaselineProfile: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("generateBaselineProfile", ignoreCase = true)
    }

internal val Project.isRunningBenchmark: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("connectedBenchmarkReleaseAndroidTest", ignoreCase = true)
    }

internal val Project.isAppleSilicon: Boolean
    get() = providers.systemProperty("os.arch").orNull == "aarch64"
