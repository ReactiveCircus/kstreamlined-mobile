package io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig.Companion.PIXEL_6
import com.android.ide.common.rendering.api.SessionParams
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.paparazzi.PaparazziRule

public class SnapshotTester(
    maxPercentDifference: Double = 0.1,
) : PaparazziRule(
    deviceConfig = PIXEL_6,
    theme = "android:Theme.Material.Light.NoActionBar",
    renderingMode = SessionParams.RenderingMode.SHRINK,
    compileSdkVersion = 34,
    maxPercentDifference = maxPercentDifference,
) {
    public fun snapshot(
        addSurface: Boolean = true,
        content: @Composable () -> Unit,
    ) {
        arrayOf(false, true).forEach { darkTheme ->
            val testNameSuffix = if (darkTheme) "Dark" else "Light"
            createFrameHandler(testNameSuffix = testNameSuffix).use { handler ->
                frameHandler = handler
                sdk.snapshot {
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
    }
}
