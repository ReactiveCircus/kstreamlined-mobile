import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.detekt)
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.RequiresOptIn")
            }
        }
    }
}

detekt {
    source.from(files("src/"))
    config.from(files("../detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_22.target
    reports {
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

dependencies {
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    // enable Ktlint formatting
    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(libs.plugin.kotlin)
    implementation(libs.plugin.compose)
    implementation(libs.plugin.serialization)
    implementation(libs.plugin.powerAssert)
    implementation(libs.plugin.ksp)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.agp)
    implementation(libs.plugin.appVersioning)
    implementation(libs.plugin.hilt)
    implementation(libs.plugin.apollo)
    implementation(libs.plugin.wire)
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

gradlePlugin {
    plugins {
        register("root") {
            id = "kstreamlined.root"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.RootPlugin"
        }
        register("androidApplication") {
            id = "kstreamlined.android.application"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "kstreamlined.android.library"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidLibraryConventionPlugin"
        }
        register("androidTest") {
            id = "kstreamlined.android.test"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidTestConventionPlugin"
        }
        register("kmpCommon") {
            id = "kstreamlined.kmp.common"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMPCommonConventionPlugin"
        }
        register("kmpAndroidAndIos") {
            id = "kstreamlined.kmp.android-and-ios"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMPAndroidAndIosConventionPlugin"
        }
        register("kmpIosOnly") {
            id = "kstreamlined.kmp.ios-only"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMPIosOnlyConventionPlugin"
        }
        register("kmpTest") {
            id = "kstreamlined.kmp.test"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMPTestConventionPlugin"
        }
        register("compose") {
            id = "kstreamlined.compose"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.ComposeConventionPlugin"
        }
        register("ksp") {
            id = "kstreamlined.ksp"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KspConventionPlugin"
        }
    }
}
