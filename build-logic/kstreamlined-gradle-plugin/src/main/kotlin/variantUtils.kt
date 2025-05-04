import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import com.android.build.api.variant.Variant
import org.gradle.api.Project
import java.io.Serializable

public fun Variant.addResValue(key: String, type: String, value: String) {
    resValues.put(makeResValueKey(type, key), ResValue(value))
}

public fun <T : Serializable> Variant.addBuildConfigField(key: String, value: T) {
    val buildConfigField = BuildConfigField(type = value::class.java.simpleName, value = value, comment = null)
    buildConfigFields?.put(key, buildConfigField)
}

public fun Project.shouldEnableVariant(variantName: String): Boolean {
    return variantName in listOf(
        "devDebug",
        "demoDebug",
        "mockDebug",
        "prodRelease",
    ) ||
        // nonMinified variant for baseline profile generation
        isGeneratingBaselineProfile && variantName == "devNonMinifiedRelease" ||
        // benchmark variants
        isRunningBenchmark && variantName in listOf("devRelease", "devBenchmarkRelease")
}
