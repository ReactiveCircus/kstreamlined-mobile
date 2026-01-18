plugins {
    id("kstreamlined")
}

kstreamlined {
    androidFeatureLibrary("io.github.reactivecircus.kstreamlined.android.feature.kotlinweeklyissue.impl") {
        screenshotTests()

        dependencies {
            implementation(project(":feature:kotlin-weekly-issue:api"))
            implementation(project(":core:launcher"))
            implementation(project(":core:navigation"))
            implementation(project(":core:ui:pattern"))
            implementation(project(":kmp:feed-datasource"))
            implementation(project(":kmp:presentation:kotlin-weekly-issue"))
        }
    }
}
