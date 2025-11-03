import dev.detekt.gradle.Detekt
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.testkit)
    idea
}

group = project.property("GROUP") as String

idea {
    module.generatedSourceDirs.add(projectDir.resolve("src/test/java"))
}

val chameleonRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }
val burstRuntimeClasspath: Configuration by configurations.creating { isTransitive = false }

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    compileOnly(kotlin("compiler"))
    compileOnly(kotlin("stdlib"))

    testImplementation(kotlin("test-junit5"))
    testImplementation(kotlin("compiler-internal-test-framework"))
    testImplementation(kotlin("compiler"))

    chameleonRuntimeClasspath(project(":chameleon:chameleon-runtime"))
    burstRuntimeClasspath(libs.burst)

    // Dependencies required to run the internal test framework.
    testRuntimeOnly(kotlin("reflect"))
    testRuntimeOnly(kotlin("test"))
    testRuntimeOnly(kotlin("script-runtime"))
    testRuntimeOnly(kotlin("annotations-jvm"))
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
        optIn.add("org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi")
        optIn.add("org.jetbrains.kotlin.ir.symbols.UnsafeDuringIrConstructionAPI")
        freeCompilerArgs.addAll("-XXLanguage:+ContextParameters")
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.register<JavaExec>("generateTests") {
    inputs
        .dir(layout.projectDirectory.dir("src/test/data"))
        .withPropertyName("testData")
        .withPathSensitivity(PathSensitivity.RELATIVE)
    outputs.dir(layout.projectDirectory.dir("src/test/java")).withPropertyName("generatedTests")

    classpath = sourceSets.test.get().runtimeClasspath
    mainClass.set("io.github.reactivecircus.chameleon.compiler.GenerateTestsKt")
    workingDir = rootDir
}

tasks.withType<Test> {
    dependsOn(chameleonRuntimeClasspath)
    inputs
        .dir(layout.projectDirectory.dir("src/test/data"))
        .withPropertyName("testData")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    useJUnitPlatform()
    workingDir = rootDir

    systemProperty("chameleonRuntime.classpath", chameleonRuntimeClasspath.asPath)
    systemProperty("burstRuntime.classpath", burstRuntimeClasspath.asPath)

    // Satisfy JDK paths normalization checks with CHECK_COMPILER_OUTPUT directive, we don't run tests on different JDKs.
    environment("JDK_1_8", "")
    environment("JDK_11_0", "")
    environment("JDK_17_0", "")
    environment("JDK_21_0", "")

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
    jvmTarget = JvmTarget.JVM_11.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
    exclude("**/test/data/**")
}
