plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("com.apollographql.apollo3")
}

apollo {
    service("service") {
        packageNamesFromFilePaths()
        codegenModels.set("responseBased")
        flattenModels.set(true)
        generateDataBuilders.set(true)
    }
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(libs.apollo.api)
            }
        }
    }
}
