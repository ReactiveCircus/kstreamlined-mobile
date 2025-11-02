import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lint)
    alias(libs.plugins.detekt)
    alias(libs.plugins.testkit)
}

group = project.property("GROUP") as String
version = project.property("VERSION_NAME") as String

gradlePlugin {
    plugins {
        register("chameleon") {
            id = "io.github.reactivecircus.chameleon"
            implementationClass = "io.github.reactivecircus.chameleon.gradle.ChameleonPlugin"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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
}

tasks.withType<Test> {
    systemProperty("io.github.reactivecircus.chameleon.gradle.test.kotlin-version", libs.versions.kotlin.get())
    systemProperty("io.github.reactivecircus.chameleon.gradle.test.burst-version", libs.versions.burst.get())
}

gradleTestKitSupport {
    withSupportLibrary()
}

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    compileOnly(libs.plugin.kotlin)

    functionalTestImplementation(libs.kotlin.test.junit)
}
