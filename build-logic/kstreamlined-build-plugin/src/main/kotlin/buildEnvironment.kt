import org.gradle.api.Project
import org.gradle.api.provider.Provider

public val Project.isCiBuild: Boolean
    get() = providers.environmentVariable("CI").orNull == "true"

public val Project.isIdeBuild: Boolean
    get() = providers.systemProperty("idea.active").orNull == "true"

public fun Project.envOrProp(name: String): Provider<String> =
    providers.environmentVariable(name).orElse(providers.gradleProperty(name).orElse(""))

public val Project.googleServicesJsonExists: Provider<Boolean>
    get() = providers.provider {
        fileTree("src").matching { it.include("**/google-services.json") }.isEmpty.not()
    }
