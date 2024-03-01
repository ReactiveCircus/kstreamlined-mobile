import com.android.build.api.variant.HasUnitTestBuilder

plugins {
    id("kstreamlined.android.library")
    id("kstreamlined.android.library.compose")
    id("kstreamlined.ksp")
}

android {
    namespace = "io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode"
    buildFeatures {
        androidResources = true
    }
}

androidComponents {
    beforeVariants {
        (it as HasUnitTestBuilder).enableUnitTest = true
    }
}

dependencies {
    implementation(project(":feature:common"))
    implementation(project(":foundation:compose-utils"))
    implementation(project(":kmp:data"))
    implementation(project(":kmp:presentation:talking-kotlin-episode"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // ExoPlayer
    implementation(libs.androidx.media3.exoplayer)

    testImplementation(kotlin("test"))
}
