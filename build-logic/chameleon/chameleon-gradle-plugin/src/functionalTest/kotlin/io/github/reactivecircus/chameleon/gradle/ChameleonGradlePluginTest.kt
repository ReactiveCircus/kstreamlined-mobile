@file:Suppress("FunctionName")

package io.github.reactivecircus.chameleon.gradle

import com.autonomousapps.kit.GradleBuilder.build
import com.autonomousapps.kit.GradleBuilder.buildAndFail
import com.autonomousapps.kit.Source
import com.autonomousapps.kit.utils.rootBuildDir
import org.gradle.testkit.runner.TaskOutcome
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ChameleonGradlePluginTest {
    @Test
    fun `generates parameterized tests for each ThemeVariant`() {
        val project = TestFixture(
            sources = listOf(
                Source.kotlin(
                    """
                        package test
                        
                        import app.cash.burst.Burst
                        import io.github.reactivecircus.chameleon.runtime.Chameleon
                        import kotlin.test.Test
                        
                        @Burst
                        @Chameleon
                        class SampleTest {
                            private val snapshotTester = SnapshotTester()
                        
                            @Test
                            fun test() {
                                snapshotTester.snapshot()
                            }
                        }
                    """.trimIndent(),
                )
                    .withPath(
                        packagePath = "test",
                        className = "SampleTest",
                    )
                    .withSourceSet("test")
                    .build(),
                SnapshotTesterSource,
            ),
        ).build()

        val result = build(project.rootDir, ":test")

        assertEquals(TaskOutcome.SUCCESS, result.task(":test")?.outcome)

        val reports = project.rootBuildDir().walkTopDown().filter {
            it.isFile && it.path.contains("test-results/test/") && it.extension == "xml"
        }.map { it.name }

        assertContains(reports, "TEST-test.SampleTest_Light.xml")
        assertContains(reports, "TEST-test.SampleTest_Dark.xml")
    }

    @Test
    fun `does not generate parameterized tests when snapshot function calls already have ThemeVariant argument`() {
        val project = TestFixture(
            sources = listOf(
                Source.kotlin(
                    """
                        package test
                        
                        import app.cash.burst.Burst
                        import io.github.reactivecircus.chameleon.runtime.Chameleon
                        import kotlin.test.Test
                        
                        @Burst
                        @Chameleon
                        class SampleTest {
                            private val snapshotTester = SnapshotTester()
                        
                            @Test
                            fun test() {
                                snapshotTester.snapshot(themeVariant = ThemeVariant.Dark)
                            }
                        }
                    """.trimIndent(),
                )
                    .withPath(
                        packagePath = "test",
                        className = "SampleTest",
                    )
                    .withSourceSet("test")
                    .build(),
                SnapshotTesterSource,
            ),
        ).build()

        val result = build(project.rootDir, ":test")

        assertEquals(TaskOutcome.SUCCESS, result.task(":test")?.outcome)

        val reports = project.rootBuildDir().walkTopDown().filter {
            it.isFile && it.path.contains("test-results/test/") && it.extension == "xml"
        }.map { it.name }

        assertEquals(reports.toList().size, 1)
        assertContains(reports, "TEST-test.SampleTest.xml")
    }

    @Test
    fun `build fails when missing Burst annotation`() {
        val project = TestFixture(
            sources = listOf(
                Source.kotlin(
                    """
                        package test
                        
                        import io.github.reactivecircus.chameleon.runtime.Chameleon
                        import kotlin.test.Test
                        
                        @Chameleon
                        class SampleTest {
                            private val snapshotTester = SnapshotTester()
                        
                            @Test
                            fun test() {
                                snapshotTester.snapshot()
                            }
                        }
                    """.trimIndent(),
                )
                    .withPath(packagePath = "test", className = "SampleTest")
                    .withSourceSet("test")
                    .build(),
                SnapshotTesterSource,
            ),
        ).build()

        val result = buildAndFail(project.rootDir, ":test")

        assertContains(result.output, "Classes annotated with `@Chameleon` must also be annotated with `@Burst`.")
    }

    @Test
    fun `build fails when snapshotFunction does not exist`() {
        val project = TestFixture(
            sources = listOf(SnapshotTesterSource),
            snapshotFunction = "test/SnapshotTester.nonExistent",
        ).build()

        val result = buildAndFail(project.rootDir, ":test")

        assertContains(result.output, "e: Could not find snapshot function <test/SnapshotTester.nonExistent>.")
    }

    @Test
    fun `build fails when themeVariant does not exist`() {
        val project = TestFixture(
            sources = listOf(SnapshotTesterSource),
            themeVariantEnum = "test/NonExistentThemeVariant",
        ).build()

        val result = buildAndFail(project.rootDir, ":test")

        assertContains(result.output, "e: Could not find theme variant enum class <test/NonExistentThemeVariant>.")
    }

    @Test
    fun `build fails when themeVariant is not an enum`() {
        val project = TestFixture(
            sources = listOf(
                Source.kotlin(
                    """
                    package test
                    
                    class SnapshotTester {
                        fun snapshot(
                            themeVariant: ThemeVariant,
                        ) {}
                    }
                    
                    class ThemeVariant
                    """.trimIndent(),
                )
                    .withPath(packagePath = "test", className = "SnapshotTester")
                    .withSourceSet("test")
                    .build(),
            ),
        ).build()

        val result = buildAndFail(project.rootDir, ":test")

        assertContains(result.output, "e: <test/ThemeVariant> is not an enum.")
    }

    @Test
    fun `build fails when themeVariant is not a parameter of the snapshotFunction`() {
        val project = TestFixture(
            sources = listOf(
                Source.kotlin(
                    """
                    package test
                    
                    class SnapshotTester {
                        fun snapshot() {}
                    }
                    
                    enum class ThemeVariant {
                        Light,
                        Dark,
                    }
                    """.trimIndent(),
                )
                    .withPath(packagePath = "test", className = "SnapshotTester")
                    .withSourceSet("test")
                    .build(),
            ),
        ).build()

        val result = buildAndFail(project.rootDir, ":test")

        assertContains(
            result.output,
            "e: Theme variant enum <test/ThemeVariant> is not a parameter of snapshot function <test/SnapshotTester.snapshot>.",
        )
    }
}
