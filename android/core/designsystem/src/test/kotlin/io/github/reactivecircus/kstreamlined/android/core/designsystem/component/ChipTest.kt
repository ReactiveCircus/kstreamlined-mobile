package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class ChipTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_Chip() {
        snapshotTester.snapshot {
            Chip(
                onClick = {},
                modifier = Modifier.padding(8.dp),
                contentColor = KSTheme.colorScheme.primary,
            ) {
                Text(
                    text = "Chip".uppercase(),
                    style = KSTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
                Icon(KSIcons.ArrowDown, contentDescription = null)
            }
        }
    }
}
