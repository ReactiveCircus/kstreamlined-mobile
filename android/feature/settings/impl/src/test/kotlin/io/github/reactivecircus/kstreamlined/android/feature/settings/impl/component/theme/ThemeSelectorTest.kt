package io.github.reactivecircus.kstreamlined.android.feature.settings.impl.component.theme

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import io.github.reactivecircus.kstreamlined.kmp.settings.model.AppSettings
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class ThemeSelectorTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_ThemeSelector(
        selectedTheme: AppSettings.Theme,
    ) {
        snapshotTester.snapshot {
            ThemeSelector(
                selectedTheme = selectedTheme,
                onSelectTheme = {},
                modifier = Modifier.Companion.padding(24.dp),
            )
        }
    }
}
