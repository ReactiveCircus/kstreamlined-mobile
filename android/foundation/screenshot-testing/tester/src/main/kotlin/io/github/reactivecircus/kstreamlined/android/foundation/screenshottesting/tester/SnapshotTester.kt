package io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import app.cash.paparazzi.TestName
import com.android.ide.common.rendering.api.SessionParams
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.paparazzi.PaparazziBridge
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

public class SnapshotTester(
    maxPercentDifference: Double = 0.1,
) : TestRule, PaparazziBridge(
    deviceConfig = PIXEL_6,
    theme = "android:Theme.Material.Light.NoActionBar",
    renderingMode = SessionParams.RenderingMode.SHRINK,
    maxPercentDifference = maxPercentDifference,
) {
    private lateinit var description: Description

    override fun apply(base: Statement, description: Description): Statement {
        this.description = description
        return object : Statement() {
            override fun evaluate() {
                base.evaluate()
            }
        }
    }

    private var currentThemeVariant: ThemeVariant? = null

    internal fun setCurrentThemeVariant(themeVariant: ThemeVariant) {
        currentThemeVariant = themeVariant
    }

    public fun snapshot(
        addSurface: Boolean = true,
        themeVariant: ThemeVariant? = null,
        content: @Composable () -> Unit,
    ) {
        val darkTheme = themeVariant == ThemeVariant.Dark || currentThemeVariant == ThemeVariant.Dark
        runPaparazzi(testName = description.toTestName()) {
            CompositionLocalProvider(LocalInspectionMode provides true) {
                KSTheme(darkTheme) {
                    if (addSurface) {
                        Surface {
                            content()
                        }
                    } else {
                        content()
                    }
                }
            }
        }
    }

    private fun Description.toTestName(): TestName {
        val fullQualifiedName = className
        val packageName = fullQualifiedName.substringBeforeLast('.', missingDelimiterValue = "")
        val className = fullQualifiedName.substringAfterLast('.')
        return TestName(packageName, className, methodName)
    }
}
