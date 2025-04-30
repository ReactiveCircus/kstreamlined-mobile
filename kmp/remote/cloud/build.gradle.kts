import com.apollographql.apollo.annotations.ApolloExperimental

plugins {
    id("kstreamlined.kmp.common")
    id("kstreamlined.kmp.test")
    id("com.apollographql.apollo")
}

apollo {
    @OptIn(ApolloExperimental::class)
    service("kstreamlined") {
        packageName.set("io.github.reactivecircus.kstreamlined.graphql")
        codegenModels.set("experimental_operationBasedWithInterfaces")
        generateMethods.set(listOf("equalsHashCode", "toString"))
        generateInputBuilders.set(true)
        generateDataBuilders.set(true)
        generateAsInternal.set(true)
        introspection {
            endpointUrl.set(envOrProp("KSTREAMLINED_API_ENDPOINT"))
        }
        mapScalar(
            graphQLName = "Instant",
            targetName = "kotlinx.datetime.Instant",
            expression = "com.apollographql.adapter.datetime.KotlinxInstantAdapter",
        )
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(project(":kmp:remote:common"))
                api(libs.apollo.runtime)
                api(libs.apollo.normalizedCache)
                implementation(libs.apollo.adapters.datetime)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kermit)
            }
        }
        commonTest {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.apollo.mockserver)
            }
        }
    }
}
