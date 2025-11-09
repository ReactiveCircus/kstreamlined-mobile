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
        register("kstreamlined") {
            id = "kstreamlined"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.KStreamlinedBuildPlugin"
        }
        register("androidApplication") {
            id = "kstreamlined.android.application"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "kstreamlined.android.library"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.AndroidLibraryConventionPlugin"
        }
        register("androidTest") {
            id = "kstreamlined.android.test"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.AndroidTestConventionPlugin"
        }
        register("androidScreenshotTest") {
            id = "kstreamlined.android.screenshot-test"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.AndroidScreenshotTestConventionPlugin"
        }
        register("kotlinJvm") {
            id = "kstreamlined.kotlin.jvm"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.KotlinJvmConventionPlugin"
        }
        register("compose") {
            id = "kstreamlined.compose"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.ComposeConventionPlugin"
        }
        register("ksp") {
            id = "kstreamlined.ksp"
            implementationClass = "io.github.reactivecircus.kstreamlined.gradle.KspConventionPlugin"
        }
    }
}

kotlin {
    explicitApi()
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType(Detekt::class.java).configureEach {
    jvmTarget = JvmTarget.JVM_24.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
}

dependencies {
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(project(":chameleon:chameleon-gradle-plugin"))

    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    // enable lint checks for Gradle plugins
    lintChecks(libs.androidx.lintGradle)

    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.compose)
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.powerAssert)
    implementation(libs.plugin.ksp)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.agp)
    implementation(libs.plugin.appVersioning)
    implementation(libs.plugin.burst)
    implementation(libs.plugin.paparazzi)
    implementation(libs.plugin.hilt)
    implementation(libs.plugin.apollo)
    implementation(libs.plugin.invert)
    implementation(libs.plugin.googleServices)
    implementation(libs.plugin.firebasePerf)
    implementation(libs.plugin.crashlytics)
    implementation(libs.plugin.appDistribution)
    implementation(libs.plugin.playPublisher)
    implementation(libs.plugin.skie)
    implementation(libs.plugin.sqldelight)
    implementation(libs.plugin.baselineprofile)
}
