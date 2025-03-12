package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Bookmarks
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.KSIcons
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon.Kotlin
import io.github.reactivecircus.kstreamlined.android.foundation.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

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
