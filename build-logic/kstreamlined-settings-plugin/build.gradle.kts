import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lint)
    alias(libs.plugins.detekt)
}

gradlePlugin {
    plugins {
        register("kstreamlinedSettings") {
            id = "kstreamlined.settings"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.KStreamlinedSettingsPlugin"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType(Detekt::class.java).configureEach {
    jvmTarget = JvmTarget.JVM_21.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
}

dependencies {
    implementation(project(":kstreamlined-build-plugin"))

    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper) {
        // TODO remove after upgrading to detekt 2.0.0-alpha.5+ (https://github.com/detekt/detekt/issues/9390)
        exclude(group = "dev.detekt", module = "ktlint-repackage")
    }

    // enable lint checks for Gradle plugins
    lintChecks(libs.androidx.lintGradle)
}
