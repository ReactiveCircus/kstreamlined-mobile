plugins {
    `kotlin-dsl`
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

dependencies {
    // TODO: remove once https://github.com/gradle/gradle/issues/15383#issuecomment-779893192 is fixed
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))

    val kotlinVersion: String = providers
        .gradleProperty("kotlinVersion")
        .getOrElse(libs.versions.kotlin.get())

    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
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
        register("kapt") {
            id = "kstreamlined.kapt"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KaptConventionPlugin"
        }
        register("ksp") {
            id = "kstreamlined.ksp"
            implementationClass = "io.github.reactivecircus.kstreamlined.buildlogic.convention.KspConventionPlugin"
        }
    }
}
