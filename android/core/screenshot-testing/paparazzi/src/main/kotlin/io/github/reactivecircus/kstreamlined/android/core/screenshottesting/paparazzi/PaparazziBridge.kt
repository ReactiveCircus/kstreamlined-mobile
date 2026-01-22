package io.github.reactivecircus.kstreamlined.android.core.screenshottesting.paparazzi

import androidx.compose.runtime.Composable
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.HtmlReportWriter
import app.cash.paparazzi.PaparazziSdk
import app.cash.paparazzi.Snapshot
import app.cash.paparazzi.SnapshotHandler
import app.cash.paparazzi.SnapshotVerifier
import app.cash.paparazzi.TestName
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import java.util.Date

/**
 * Adapted from https://github.com/cashapp/paparazzi/blob/master/paparazzi/src/main/java/app/cash/paparazzi/Paparazzi.kt
 * to support more opinionated test rule implementation.
 */
public abstract class PaparazziBridge(
    private val deviceConfig: DeviceConfig,
    private val theme: String,
    private val renderingMode: SessionParams.RenderingMode,
    private val compileSdkVersion: Int? = null,
    maxPercentDifference: Double,
) {
    private lateinit var sdk: PaparazziSdk
    private lateinit var frameHandler: SnapshotHandler.FrameHandler
    private val snapshotHandler: SnapshotHandler = determineHandler(maxPercentDifference)
    private var testName: TestName? = null

    protected fun runPaparazzi(
        testName: TestName,
        content: @Composable () -> Unit,
    ) {
        sdk = PaparazziSdk(
            environment = detectEnvironment().let {
                if (compileSdkVersion != null) {
                    it.copy(compileSdkVersion = compileSdkVersion)
                } else {
                    it
                }
            },
            deviceConfig = deviceConfig,
            theme = theme,
            renderingMode = renderingMode,
            appCompatEnabled = false,
            onNewFrame = { frameHandler.handle(it) },
        )
        sdk.setup()
        prepare(testName)
        try {
            createFrameHandler().use { handler ->
                frameHandler = handler
                sdk.snapshot(content)
            }
        } finally {
            close()
        }
    }

    private fun prepare(testName: TestName) {
        this.testName = testName
        sdk.prepare()
    }

    private fun close() {
        testName = null
        sdk.teardown()
        snapshotHandler.close()
    }

    private fun createFrameHandler(
        name: String? = null,
        frameCount: Int = 1,
        fps: Int = -1,
    ): SnapshotHandler.FrameHandler {
        val snapshot = Snapshot(
            name = name,
            testName = testName!!,
            timestamp = Date(),
        )
        return snapshotHandler.newFrameHandler(snapshot, frameCount, fps)
    }

    private companion object {
        private val isVerifying: Boolean =
            System.getProperty("paparazzi.test.verify")?.toBoolean() == true

        private fun determineHandler(maxPercentDifference: Double): SnapshotHandler =
            if (isVerifying) {
                SnapshotVerifier(maxPercentDifference)
            } else {
                HtmlReportWriter(maxPercentDifference = maxPercentDifference)
            }
    }
}
