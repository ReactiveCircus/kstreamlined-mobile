import dev.detekt.gradle.Detekt
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    idea
}

val sharedProps = layout.projectDirectory.file("../gradle.properties").asFile
if (sharedProps.exists()) {
    val props = Properties().apply {
        sharedProps.inputStream().use { load(it) }
    }
    group = props.getProperty("GROUP")
}

idea {
    module.generatedSourceDirs.add(projectDir.resolve("src/test/java"))
}

val routeBindingRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }
val metroRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }
val composeRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    compileOnly(kotlin("compiler"))
    compileOnly(kotlin("stdlib"))
    compileOnly(libs.metro.compiler)

    routeBindingRuntimeClasspath(project(":routebinding:routebinding-runtime"))
    metroRuntimeClasspath(libs.metro.runtime)
    composeRuntimeClasspath(libs.androidx.compose.runtime)

    testImplementation(libs.metro.compiler)
    testImplementation(libs.composeCompiler)
    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("compiler-internal-test-framework"))
    testImplementation(kotlin("compiler"))

    // Dependencies required to run the internal test framework.
    testRuntimeOnly(kotlin("reflect"))
    testRuntimeOnly(kotlin("test"))
    testRuntimeOnly(kotlin("script-runtime"))
    testRuntimeOnly(kotlin("annotations-jvm"))
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.register<JavaExec>("generateTests") {
    inputs
        .dir(layout.projectDirectory.dir("src/test/data"))
        .withPropertyName("testData")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    outputs.dir(layout.projectDirectory.dir("src/test/java")).withPropertyName("generatedTests")

    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("io.github.reactivecircus.routebinding.compiler.GenerateTestsKt")
    workingDir = rootDir
}

tasks.withType<Test> {
    dependsOn(routeBindingRuntimeClasspath)
    inputs
        .dir(layout.projectDirectory.dir("src/test/data"))
        .withPropertyName("testData")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    useJUnitPlatform()
    workingDir = rootDir

    systemProperty("routeBindingRuntime.classpath", routeBindingRuntimeClasspath.asPath)
    systemProperty("metroRuntime.classpath", metroRuntimeClasspath.asPath)
    systemProperty("composeRuntime.classpath", composeRuntimeClasspath.asPath)

    // Properties required to run the internal test framework.
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib", "kotlin-stdlib")
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-stdlib-jdk8", "kotlin-stdlib-jdk8")
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-reflect", "kotlin-reflect")
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-test", "kotlin-test")
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-script-runtime", "kotlin-script-runtime")
    setLibraryProperty("org.jetbrains.kotlin.test.kotlin-annotations-jvm", "kotlin-annotations-jvm")

    systemProperty("idea.ignore.disabled.plugins", "true")
    systemProperty("idea.home.path", rootDir)
}

fun Test.setLibraryProperty(propName: String, jarName: String) {
    val path = project.configurations
        .testRuntimeClasspath.get()
        .files
        .find { """$jarName-\d.*jar""".toRegex().matches(it.name) }
        ?.absolutePath
        ?: return
    systemProperty(propName, path)
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_21.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
    exclude("**/test/data/**")
}
