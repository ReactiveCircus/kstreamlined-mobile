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
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(project(":aab-publisher"))
    implementation(project(":chameleon:chameleon-gradle-plugin"))
    implementation(project(":licentia:licentia-gradle-plugin"))
    implementation(project(":v2p"))

    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper)

    // enable lint checks for Gradle plugins
    lintChecks(libs.androidx.lintGradle)

    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.compose)
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.powerAssert)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.agp)
    implementation(libs.plugin.appVersioning)
    implementation(libs.plugin.burst)
    implementation(libs.plugin.licensee)
    implementation(libs.plugin.paparazzi)
    implementation(libs.plugin.metro)
    implementation(libs.plugin.apollo)
    implementation(libs.plugin.invert)
    implementation(libs.plugin.googleServices)
    implementation(libs.plugin.firebasePerf)
    implementation(libs.plugin.crashlytics)
    implementation(libs.plugin.appDistribution)
    implementation(libs.plugin.skie)
    implementation(libs.plugin.sqldelight)
    implementation(libs.plugin.baselineprofile)
}
