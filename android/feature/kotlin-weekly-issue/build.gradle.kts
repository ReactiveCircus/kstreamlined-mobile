plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue") {
        screenshotTests()

        dependencies {
            implementation(project(":core:launcher"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:kotlin-weekly-issue"))
        }
    }
}
