package io.github.reactivecircus.kstreamlined.gradle.internal

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import dev.zacsweers.metro.gradle.DelicateMetroGradleApi
import dev.zacsweers.metro.gradle.ExperimentalMetroGradleApi
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.SetProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Locale

/**
 * Apply and configure Metro plugin.
 */
@OptIn(DelicateMetroGradleApi::class, ExperimentalMetroGradleApi::class)
internal fun Project.configureMetro() {
    pluginManager.apply("dev.zacsweers.metro")
    extensions.configure(MetroPluginExtension::class.java) {
        it.generateContributionProviders.set(true)
        if (providers.gradleProperty("enableMetroCompilerReports").orNull == "true") {
            it.reportsDestination.set(layout.buildDirectory.dir("metro_reports"))
        }
    }
}

/**
 * Register a verification task on the application's devDebug variant that verifies all project dependencies
 * containing Metro contribution hints are on the compileClasspath (not just runtime).
 */
internal fun ApplicationAndroidComponentsExtension.configureMetroContributionVerification(
    project: Project,
) = onVariants(selector().withName("devDebug")) { variant ->
    val variantName = variant.name
    val taskName = "verifyMetroContributions${variantName.capitalizeFirstChar()}"

    val compileClasspath = project.configurations.named("${variantName}CompileClasspath")
    val runtimeClasspath = project.configurations.named("${variantName}RuntimeClasspath")

    project.tasks.register(taskName, VerifyMetroContributionsTask::class.java) { task ->
        val projectName = project.displayName
        task.description = "Verifies all Metro contribution hints are on the compile classpath of $projectName."
        task.group = "verification"
        task.dependsOn("compile${variantName.capitalizeFirstChar()}Kotlin")

        task.compileClasspathProjectPaths.set(
            compileClasspath.map { config ->
                config.incoming.resolutionResult.allComponents
                    .mapNotNull { (it.id as? ProjectComponentIdentifier)?.projectPath }
                    .toSet()
            },
        )

        val rootDir = project.rootDir
        task.runtimeOnlyProjectBuildDirs.set(
            project.provider {
                val compilePaths = compileClasspath.get().incoming.resolutionResult.allComponents
                    .mapNotNull { (it.id as? ProjectComponentIdentifier)?.projectPath }
                    .toSet()

                runtimeClasspath.get().incoming.resolutionResult.allComponents
                    .mapNotNull { it.id as? ProjectComponentIdentifier }
                    .filter { it.projectPath !in compilePaths }
                    .associate { id ->
                        id.projectPath to buildDirForProjectPath(rootDir, id.projectPath)
                    }
            },
        )

        task.reportFile.set(
            project.layout.buildDirectory.file("reports/metro-contributions-verification-$variantName.txt"),
        )
    }

    project.tasks.named("check").configure { it.dependsOn(taskName) }
}

private fun buildDirForProjectPath(rootDir: File, projectPath: String): String {
    val relativePath = projectPath.removePrefix(":").replace(":", "/")
    val defaultBuildDir = File(rootDir, "$relativePath/build")
    if (defaultBuildDir.isDirectory) return defaultBuildDir.absolutePath
    val androidBuildDir = File(rootDir, "android/$relativePath/build")
    if (androidBuildDir.isDirectory) return androidBuildDir.absolutePath
    return defaultBuildDir.absolutePath
}

private fun String.capitalizeFirstChar(): String =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.US) else it.toString() }

/**
 * Verifies that all project dependencies contributing Metro bindings (those with generated
 * `metro.hints` classes) are on the compileClasspath, not just the runtimeClasspath.
 *
 * Metro discovers contributed bindings via hint classes in the `metro.hints` package during
 * compilation of `@DependencyGraph`-annotated classes. If a contributing module is only on
 * the runtime classpath (e.g. transitive via `implementation`), its contributions won't be
 * discovered and the app might crash at runtime for multibindings with `@Multibinds(allowEmpty = true)`.
 */
private abstract class VerifyMetroContributionsTask : DefaultTask() {
    /**
     * Project paths that are on the compileClasspath.
     */
    @get:Input
    abstract val compileClasspathProjectPaths: SetProperty<String>

    /**
     * Build directory paths of projects that are on the compileClasspath but NOT the runtimeClasspath.
     */
    @get:Input
    abstract val runtimeOnlyProjectBuildDirs: MapProperty<String, String>

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val missingProjects = runtimeOnlyProjectBuildDirs.get()
            .filter { (_, buildDir) -> containsMetroHints(File(buildDir)) }
            .keys
            .sorted()

        val report = reportFile.get().asFile
        report.parentFile.mkdirs()

        if (missingProjects.isNotEmpty()) {
            val message = buildString {
                appendLine(
                    """
                    |Metro contributions verification failed.
                    |
                    |The following projects contain Metro contribution hints (`metro.hints` package)
                    |but are only on the runtime classpath, not the compile classpath.
                    |Metro requires contributed bindings to be on the compile classpath to discover them.
                    |
                    |Add these as direct dependencies of the project containing your `@DependencyGraph`:
                    |
                    """.trimMargin(),
                )
                for (path in missingProjects) {
                    appendLine("implementation(project(\"$path\"))")
                }
            }
            report.writeText(message)
            error(message)
        }

        report.writeText("All Metro contributions are on the compile classpath.\n")
    }

    /**
     * Check if a project's build output contains Metro contribution hints.
     * Scans `classes/kotlin/{target}/{compilation}/metro/hints/` directories.
     */
    private fun containsMetroHints(buildDir: File): Boolean {
        val classesDir = File(buildDir, "classes")
        if (!classesDir.isDirectory) return false
        return classesDir.walk()
            .any {
                it.isDirectory &&
                    it.name == "hints" &&
                    it.parentFile?.name == "metro" &&
                    it.listFiles()?.isNotEmpty() == true
            }
    }
}
