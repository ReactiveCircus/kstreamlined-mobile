@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Chameleon
class ModalBottomSheetTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_ModalBottomSheet() {
        snapshotTester.snapshot {
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = rememberModalBottomSheetState(initiallyExpanded = true),
            ) {
                Text(
                    text = "Sheet content",
                    style = KSTheme.typography.titleMedium,
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        }
    }
}
