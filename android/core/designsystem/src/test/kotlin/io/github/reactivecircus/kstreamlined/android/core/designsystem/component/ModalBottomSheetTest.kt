@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.burst.Burst
import io.github.reactivecircus.chameleon.runtime.Chameleon
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.screenshottesting.tester.SnapshotTester
import org.junit.Rule
import org.junit.Test

@Burst
@Chameleon
class ModalBottomSheetTest {
    @get:Rule
    val snapshotTester = SnapshotTester()

    @Test
    fun snapshot_ModalBottomSheet() {
        snapshotTester.snapshot {
            val m3SheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Expanded)
            ModalBottomSheet(
                onDismissRequest = {},
                sheetState = SheetState(m3SheetState),
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
