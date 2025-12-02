import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
        sqlDelightDatabase("KStreamlinedDatabase") {
            packageName.set("io.github.reactivecircus.kstreamlined.kmp.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        dependencies {
            api(libs.sqldelight.coroutinesExtensions)
        }
    }
}
