plugins {
    id("kstreamlined.kmm.jvm-and-ios")
    id("com.apollographql.apollo3")
}

apollo {
    packageNamesFromFilePaths()
    codegenModels.set("responseBased")
    flattenModels.set(true)
    generateTestBuilders.set(true)
    testDirConnection {
        connectToKotlinSourceSet("commonMain")
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
