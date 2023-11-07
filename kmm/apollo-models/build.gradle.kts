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
        commonMain {
            dependencies {
                api(libs.apollo.api)
            }
        }
    }
}
