import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import com.android.build.api.variant.Variant
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import java.io.Serializable

public fun Variant.addResValueFromString(key: String, value: String) {
    resValues.put(makeResValueKey(type = "string", key), ResValue(value))
}

public fun Variant.addResValueFromBoolean(key: String, value: Boolean) {
    resValues.put(makeResValueKey(type = "bool", key), ResValue(value.toString()))
}

public fun <T : Serializable> Variant.addBuildConfigField(key: String, value: T) {
    buildConfigFields?.put(
        key,
        if (value is String) {
            BuildConfigField(type = value::class.java.simpleName, value = "\"$value\"", comment = null)
        } else {
            BuildConfigField(type = value::class.java.simpleName, value = value, comment = null)
        },
    )
}

public fun <T : Serializable> Variant.addBuildConfigField(key: String, value: Provider<T>) {
    buildConfigFields?.put(
        key,
        value.map {
            if (it is String) {
                BuildConfigField(type = it::class.java.simpleName, value = "\"$it\"", comment = null)
            } else {
                BuildConfigField(type = it::class.java.simpleName, value = it, comment = null)
            }
        },
    )
}

public fun Project.shouldEnableVariant(variantName: String): Boolean = variantName in listOf(
    "devDebug",
    "demoDebug",
    "mockDebug",
    "prodRelease",
) ||
    // nonMinified variant for baseline profile generation
    isGeneratingBaselineProfile && variantName == "devNonMinifiedRelease" ||
    // benchmark variants
    isRunningBenchmark && variantName in listOf("devRelease", "devBenchmarkRelease")

private val Project.isGeneratingBaselineProfile: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("generateBaselineProfile", ignoreCase = true)
    }

private val Project.isRunningBenchmark: Boolean
    get() = gradle.startParameter.taskNames.any {
        it.contains("connectedBenchmarkReleaseAndroidTest", ignoreCase = true)
    }
