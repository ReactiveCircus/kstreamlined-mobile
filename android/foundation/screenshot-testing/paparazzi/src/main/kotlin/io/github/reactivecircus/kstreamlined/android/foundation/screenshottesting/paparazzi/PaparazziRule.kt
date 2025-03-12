package io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.paparazzi

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.HtmlReportWriter
import app.cash.paparazzi.PaparazziSdk
import app.cash.paparazzi.Snapshot
import app.cash.paparazzi.SnapshotHandler
import app.cash.paparazzi.SnapshotVerifier
import app.cash.paparazzi.TestName
import app.cash.paparazzi.detectEnvironment
import com.android.ide.common.rendering.api.SessionParams
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.util.Date

public abstract class PaparazziRule(
    private val deviceConfig: DeviceConfig,
    private val theme: String,
    private val renderingMode: SessionParams.RenderingMode,
    private val compileSdkVersion: Int,
    maxPercentDifference: Double,
) : TestRule {
    protected lateinit var sdk: PaparazziSdk
    protected lateinit var frameHandler: SnapshotHandler.FrameHandler
    private var testName: TestName? = null

    private val snapshotHandler: SnapshotHandler = determineHandler(maxPercentDifference)

    override fun apply(base: Statement, description: Description): Statement =
        object : Statement() {
            override fun evaluate() {
                Description.createTestDescription(
                    description.className,
                    description.methodName
                )
                sdk = PaparazziSdk(
                    environment = detectEnvironment().copy(compileSdkVersion = compileSdkVersion),
                    deviceConfig = deviceConfig,
                    theme = theme,
                    renderingMode = renderingMode,
                    appCompatEnabled = false,
                    onNewFrame = { frameHandler.handle(it) },
                )
                sdk.setup()
                prepare(description)
                try {
                    base.evaluate()
                } finally {
                    close()
                }
            }
        }

    private fun prepare(description: Description) {
        testName = description.toTestName()
        sdk.prepare()
    }

    private fun close() {
        testName = null
        sdk.teardown()
        snapshotHandler.close()
    }

    protected fun createFrameHandler(
        name: String? = null,
        testNameSuffix: String? = null,
        frameCount: Int = 1,
        fps: Int = -1,
    ): SnapshotHandler.FrameHandler {
        val snapshot = Snapshot(
            name = name,
            testName = testName!!.copy(
                methodName = testNameSuffix?.let {
                    testName!!.methodName + "[$testNameSuffix]"
                } ?: testName!!.methodName
            ),
            timestamp = Date(),
        )
        return snapshotHandler.newFrameHandler(snapshot, frameCount, fps)
    }

    private fun Description.toTestName(): TestName {
        val fullQualifiedName = className
        val packageName = fullQualifiedName.substringBeforeLast('.', missingDelimiterValue = "")
        val className = fullQualifiedName.substringAfterLast('.')
        return TestName(packageName, className, methodName)
    }

    private companion object {
        private val isVerifying: Boolean =
            System.getProperty("paparazzi.test.verify")?.toBoolean() == true

        private fun determineHandler(maxPercentDifference: Double): SnapshotHandler =
            if (isVerifying) {
                SnapshotVerifier(maxPercentDifference)
            } else {
                HtmlReportWriter()
            }
    }
}
