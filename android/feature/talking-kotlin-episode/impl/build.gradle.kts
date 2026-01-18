plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.talkingkotlinepisode.impl") {
        unitTests()
        screenshotTests()

        dependencies {
            implementation(project(":feature:talking-kotlin-episode:api"))
            implementation(project(":core:launcher"))
            implementation(project(":core:navigation"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":core:ui:util"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:talking-kotlin-episode"))
            implementation(libs.androidx.media3.exoplayer)
        }
    }
}
