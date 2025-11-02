package io.github.reactivecircus.chameleon.gradle

import com.autonomousapps.kit.AbstractGradleProject
import com.autonomousapps.kit.AbstractGradleProject.Companion.PLUGIN_UNDER_TEST_VERSION
import com.autonomousapps.kit.GradleProject
import com.autonomousapps.kit.Source
import com.autonomousapps.kit.gradle.Dependency.Companion.testImplementation
import com.autonomousapps.kit.gradle.Plugin

class TestFixture(
    private val sources: List<Source>,
    private val snapshotFunction: String = "test/SnapshotTester.snapshot",
    private val themeVariantEnum: String = "test/ThemeVariant",
) : AbstractGradleProject() {
    fun build(): GradleProject {
        return newGradleProjectBuilder(GradleProject.DslKind.KOTLIN)
            .withRootProject {
                sources = this@TestFixture.sources
                withBuildScript {
                    plugins(
                        Plugins.KotlinJvm,
                        Plugins.Chameleon,
                        Plugins.Burst,
                    )
                    withKotlin(
                        buildString {
                            appendLine("chameleon {")
                            appendLine(
                                """
                                    snapshotFunction.set("$snapshotFunction")
                                    themeVariantEnum.set("$themeVariantEnum")
                                """.trimIndent(),
                            )
                            appendLine("}")
                        },
                    )
                    dependencies(
                        testImplementation("org.jetbrains.kotlin:kotlin-test-junit"),
                    )
                }
            }
            .write()
    }
}

private object Plugins {
    val KotlinJvm = Plugin(
        id = "org.jetbrains.kotlin.jvm",
        version = System.getProperty("io.github.reactivecircus.chameleon.gradle.test.kotlin-version"),
    )
    val Chameleon = Plugin(
        id = "io.github.reactivecircus.chameleon",
        version = PLUGIN_UNDER_TEST_VERSION,
    )
    val Burst = Plugin(
        id = "app.cash.burst",
        version = System.getProperty("io.github.reactivecircus.chameleon.gradle.test.burst-version"),
    )
}

val SnapshotTesterSource = Source.kotlin(
    """
    package test

    class SnapshotTester {
        fun snapshot(
            themeVariant: ThemeVariant = ThemeVariant.Light,
        ) {}
    }

    enum class ThemeVariant {
        Light,
        Dark,
    }
    """.trimIndent(),
)
    .withPath(packagePath = "test", className = "SnapshotTester")
    .withSourceSet("test")
    .build()
