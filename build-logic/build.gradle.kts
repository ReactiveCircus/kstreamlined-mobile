import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(20))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

tasks.withType<KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
    }
}

tasks.withType<JavaCompile>().configureEach {
    sourceCompatibility = JavaVersion.VERSION_17.toString()
    targetCompatibility = JavaVersion.VERSION_17.toString()
}

detekt {
    source.from(files("src/"))
    config.from(files("../detekt.yml"))
    buildUponDefaultConfig = true
    allRules = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_17.target
    reports {
        xml.required.set(false)
        txt.required.set(false)
        sarif.required.set(false)
        md.required.set(false)
    }
}

val kotlinVersion: Provider<String> = providers
    .gradleProperty("kotlinVersion")
    .orElse(libs.versions.kotlin)

dependencies {
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    // enable Ktlint formatting
    add("detektPlugins", libs.plugin.detektFormatting)

    implementation(libs.plugin.kotlin.flatMap { dependency ->
        kotlinVersion.map { version ->
            "${dependency.group}:${dependency.name}:$version"
        }
    })
    implementation(libs.plugin.ksp)
    implementation(libs.plugin.detekt)
    implementation(libs.plugin.agp)
    implementation(libs.plugin.appVersioning)
    implementation(libs.plugin.hilt)
    implementation(libs.plugin.apollo)
    implementation(libs.plugin.wire)
    implementation(libs.plugin.googleServices)
    implementation(libs.plugin.firebasePerf)
    implementation(libs.plugin.crashlytics)
    implementation(libs.plugin.appDistribution)
    implementation(libs.plugin.playPublisher)
    implementation(libs.plugin.fladle)
    implementation(libs.plugin.nativeCoroutines)
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
        register("androidApplicationCompose") {
            id = "kstreamlined.android.application.compose"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "kstreamlined.android.library"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "kstreamlined.android.library.compose"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidLibraryComposeConventionPlugin"
        }
        register("androidTest") {
            id = "kstreamlined.android.test"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.AndroidTestConventionPlugin"
        }
        register("kmmJvmAndIos") {
            id = "kstreamlined.kmm.jvm-and-ios"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMMJvmAndIosConventionPlugin"
        }
        register("kmmIosOnly") {
            id = "kstreamlined.kmm.ios-only"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMMIosOnlyConventionPlugin"
        }
        register("kmmTest") {
            id = "kstreamlined.kmm.test"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KMMTestConventionPlugin"
        }
        register("ksp") {
            id = "kstreamlined.ksp"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KspConventionPlugin"
        }
    }
}
