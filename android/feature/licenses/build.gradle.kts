import com.android.build.api.variant.HasUnitTestBuilder

plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.screenshot-test")
    id("kstreamlined.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.licenses"
    androidResources.enable = true
}

androidComponents {
    beforeVariants {
        (it as HasUnitTestBuilder).enableUnitTest = true
    }
}

dependencies {
    implementation(project(":kmp:presentation:common"))
    implementation(project(":feature:common"))
    implementation(project(":kmp:app-info"))

    implementation(libs.androidx.tracing)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}
