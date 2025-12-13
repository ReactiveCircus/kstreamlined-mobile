package io.github.reactivecircus.kstreamlined.gradle.internal

import app.cash.licensee.LicenseeExtension
import app.cash.licensee.UnusedAction
import org.gradle.api.Project

/**
 * Configure licenses info generation with licensee and licentia plugins.
 */
internal fun Project.configureLicensesInfoGeneration() {
    pluginManager.apply("app.cash.licensee")
    extensions.configure(LicenseeExtension::class.java) {
        it.bundleAndroidAsset.set(true)
        it.androidAssetReportPath.set("licensee/artifacts.json")
        it.allow("Apache-2.0")
        it.allow("MIT")
        it.allow("BSD-3-Clause")
        it.allowUrl("https://opensource.org/license/MIT")
        it.allowUrl("https://developer.android.com/studio/terms.html")
        it.unusedAction(UnusedAction.IGNORE)
    }
    pluginManager.apply("io.github.reactivecircus.licentia")
}
