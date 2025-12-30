plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.savedforlater") {
        screenshotTests()

        dependencies {
            implementation(project(":feature:content-viewer:api"))
            implementation(project(":feature:talking-kotlin-episode:api"))
            implementation(project(":feature:settings:api"))
            implementation(project(":core:ui:feed"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:saved-for-later"))
        }
    }
}
