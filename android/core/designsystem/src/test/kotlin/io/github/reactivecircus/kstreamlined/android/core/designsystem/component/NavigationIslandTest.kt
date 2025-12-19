package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class NavigationIslandTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_NavigationIsland() {
        snapshotTester.snapshot {
            NavigationIsland(
                modifier = Modifier.padding(8.dp),
            ) {
                NavigationIslandItem(
                    selected = true,
                    icon = KSIcons.Kotlin,
                    contentDescription = "Home",
                    onClick = {},
                )
                NavigationIslandDivider()
                NavigationIslandItem(
                    selected = false,
                    icon = KSIcons.Bookmarks,
                    contentDescription = "Saved",
                    onClick = {},
                )
            }
        }
    }
}
