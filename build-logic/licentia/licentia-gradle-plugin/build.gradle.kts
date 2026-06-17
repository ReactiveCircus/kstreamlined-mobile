import dev.detekt.gradle.Detekt
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    `java-gradle-plugin`
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.lint)
    alias(libs.plugins.detekt)
}

val sharedProps = layout.projectDirectory.file("../gradle.properties").asFile
if (sharedProps.exists()) {
    val props = Properties().apply {
        sharedProps.inputStream().use { load(it) }
    }
    group = props.getProperty("GROUP")
}

gradlePlugin {
    plugins {
        register("licentia") {
            id = "io.github.reactivecircus.licentia"
            implementationClass = "io.github.reactivecircus.licentia.gradle.LicentiaGradlePlugin"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
    explicitApi()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

detekt {
    source.setFrom(file("src/"))
    config.setFrom(file("$rootDir/../detekt.yml"))
    buildUponDefaultConfig = true
    parallel = true
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = JvmTarget.JVM_21.target
    reports {
        checkstyle.required.set(false)
        sarif.required.set(false)
        markdown.required.set(false)
    }
}

dependencies {
    // enable Ktlint formatting
    detektPlugins(libs.plugin.detektKtlintWrapper) {
        // TODO remove after upgrading to detekt 2.0.0-alpha.5+ (https://github.com/detekt/detekt/issues/9390)
        exclude(group = "dev.detekt", module = "ktlint-repackage")
    }

    compileOnly(libs.plugin.agp)
    compileOnly(libs.plugin.licensee)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinpoet)

    testImplementation(libs.kotlin.test.junit)
}
