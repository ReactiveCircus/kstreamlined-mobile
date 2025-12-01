package io.github.reactivecircus.kstreamlined.android.feature.licenses.component

import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.feature.licenses.ArtifactLicenseItem
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import kotlin.test.Test

@Burst
@Chameleon
class ArtifactLicenseRowTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_ArtifactLicenseRow() {
        snapshotTester.snapshot {
            ArtifactLicenseRow(
                item = ArtifactLicenseItem(
                    title = "Compose UI",
                    description = "androidx:compose.ui:ui",
                    version = "1.10.0",
                    scmUrl = "url",
                    licenses = listOf("Apache License 2.0"),
                ),
            )
        }
    }
}
