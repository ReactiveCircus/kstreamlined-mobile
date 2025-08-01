import com.apollographql.apollo.annotations.ApolloExperimental
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined.kmp.jvm-and-ios")
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
            targetName = "kotlin.time.Instant",
            expression = "com.apollographql.adapter.core.KotlinInstantAdapter",
        )
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(project(":kmp:remote:common"))
        api(libs.apollo.runtime)
        api(libs.apollo.normalizedCache)
        implementation(libs.apollo.adapters.core)
        implementation(libs.kotlinx.coroutines.core)
        implementation(libs.kermit)

        testImplementation(libs.kotlinx.coroutines.test)
        testImplementation(libs.apollo.mockserver)
    }
}
