import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
    alias(libs.plugins.testkit)
}

group = project.property("GROUP") as String
version = project.property("VERSION_NAME") as String

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

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)
}
