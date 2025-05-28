import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    id("kstreamlined")
    id("app.cash.sqldelight")
}

kstreamlined {
    kmpLibrary {
        targets {
            jvm()
            ios()
        }
    }
}

sqldelight {
    databases {
        create("KStreamlinedDatabase") {
            packageName.set("io.github.reactivecircus.kstreamlined.kmp.database")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/databases"))
        }
    }
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    dependencies {
        api(libs.sqldelight.coroutinesExtensions)
    }
}
