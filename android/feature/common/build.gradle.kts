import com.android.build.api.variant.HasUnitTestBuilder

plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.common"
    buildFeatures {
        androidResources = true
    }
}

androidComponents {
    beforeVariants {
        (it as HasUnitTestBuilder).enableUnitTest = false
        @Suppress("UnstableApiUsage")
        it.androidTest.enable = false
    }
}

dependencies {
    api(project(":designsystem"))

    // AndroidX
    api(libs.androidx.core)
    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.ui.tooling)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewmodelCompose)
    implementation(libs.androidx.browser)

    // Image loading
    api(libs.coil)

    // Logging
    api(libs.kermit)

    // Coroutines
    api(libs.kotlinx.coroutines.core)
}
