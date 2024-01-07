import io.github.reactivecircus.kstreamlined.buildlogic.envOrProp

plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("com.apollographql.apollo3")
}

apollo {
    service("kstreamlined") {
        packageName.set("io.github.reactivecircus.kstreamlined.graphql")
        codegenModels.set("experimental_operationBasedWithInterfaces")
        generateMethods.set(listOf("equalsHashCode", "toString"))
        generateInputBuilders.set(true)
        generateDataBuilders.set(true)
        introspection {
            endpointUrl.set(envOrProp("KSTREAMLINED_API_ENDPOINT"))
        }
        mapScalar(
            graphQLName = "Instant",
            targetName = "kotlinx.datetime.Instant",
            expression = "com.apollographql.apollo3.adapter.KotlinxInstantAdapter",
        )
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:networking:common"))
                api(libs.apollo.runtime)
                api(libs.apollo.normalizedCache)
                implementation(libs.apollo.adapters)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
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
