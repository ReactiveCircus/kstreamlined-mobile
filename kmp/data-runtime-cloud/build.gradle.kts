plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("com.apollographql.apollo3")
}

apollo {
    service("kstreamlined") {
        packageName.set("io.github.reactivecircus.kstreamlined.graphql")
        codegenModels.set("responseBased")
        flattenModels.set(true)
        generateDataBuilders.set(true)
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:data-common"))
                api(libs.apollo.runtime)
                api(libs.apollo.normalizedCache)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(project(":kmp:test-utils"))
                implementation(libs.apollo.testingSupport)
            }
        }
    }
}
