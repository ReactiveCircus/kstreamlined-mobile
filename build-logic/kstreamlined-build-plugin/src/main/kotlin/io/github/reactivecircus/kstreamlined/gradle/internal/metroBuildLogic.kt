package io.github.reactivecircus.kstreamlined.gradle.internal

import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import dev.zacsweers.metro.gradle.DelicateMetroGradleApi
import dev.zacsweers.metro.gradle.MetroPluginExtension
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.artifacts.component.ProjectComponentIdentifier
import org.gradle.api.artifacts.type.ArtifactTypeDefinition
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.Classpath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.util.Locale
import java.util.zip.ZipFile

/**
 * Apply and configure Metro plugin.
 */
@OptIn(DelicateMetroGradleApi::class)
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
context(project: Project)
internal fun ApplicationAndroidComponentsExtension.configureMetroContributionVerification() =
    onVariants(selector().withName("devDebug")) { variant ->
        val variantName = variant.name
        val taskName = "verifyMetroContributions${variantName.capitalizeFirstChar()}"

        val compileClasspath = project.configurations.named("${variantName}CompileClasspath")
        val runtimeClasspath = project.configurations.named("${variantName}RuntimeClasspath")

        project.tasks.register(taskName, VerifyMetroContributionsTask::class.java) { task ->
            val projectName = project.displayName
            task.description = "Verifies all Metro contribution hints are on the compile classpath of $projectName."
            task.group = "verification"

            val compileClasspathProjectPaths = compileClasspath.map { config ->
                config.incoming.resolutionResult.allComponents
                    .mapNotNull { (it.id as? ProjectComponentIdentifier)?.projectPath }
                    .toSet()
            }
            val runtimeClassJars = runtimeClasspath.map { config ->
                config.incoming.artifactView { view ->
                    view.attributes.attribute(
                        ArtifactTypeDefinition.ARTIFACT_TYPE_ATTRIBUTE,
                        "android-classes-jar",
                    )
                    view.componentFilter { it is ProjectComponentIdentifier }
                }
            }
            val runtimeProjectArtifacts = runtimeClassJars.flatMap { view ->
                view.artifacts.resolvedArtifacts
            }

            task.runtimeOnlyProjectArtifacts.set(
                compileClasspathProjectPaths.zip(runtimeProjectArtifacts) { compilePaths, runtimeArtifacts ->
                    runtimeArtifacts
                        .mapNotNull { artifact ->
                            val id = artifact.id.componentIdentifier as? ProjectComponentIdentifier
                                ?: return@mapNotNull null
                            if (id.projectPath in compilePaths) return@mapNotNull null
                            id.projectPath to artifact.file.absolutePath
                        }
                        .toMap()
                },
            )
            task.runtimeProjectClassJars.from(
                runtimeClassJars.map { it.files },
            )

            task.reportFile.set(
                project.layout.buildDirectory.file("reports/metro-contributions-verification-$variantName.txt"),
            )
        }

        project.tasks.named("check").configure { it.dependsOn(taskName) }
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
     * Runtime-only project paths and their resolved Android runtime artifacts.
     */
    @get:Input
    abstract val runtimeOnlyProjectArtifacts: MapProperty<String, String>

    /**
     * Resolved project runtime artifacts, carrying their producing task dependencies.
     */
    @get:Classpath
    abstract val runtimeProjectClassJars: ConfigurableFileCollection

    @get:OutputFile
    abstract val reportFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val missingProjects = runtimeOnlyProjectArtifacts.get()
            .filter { (_, artifactPath) -> containsMetroHints(File(artifactPath)) }
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
     * Check if a resolved project artifact contains Metro contribution hints.
     */
    private fun containsMetroHints(artifactJar: File): Boolean =
        ZipFile(artifactJar).use { zip ->
            zip.entries().asSequence()
                .any { !it.isDirectory && it.name.startsWith("metro/hints/") }
        }
}
