@file:OptIn(ExperimentalMaterial3Api::class)

package io.github.reactivecircus.kstreamlined.android.core.designsystem.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue.Expanded
import androidx.compose.material3.SheetValue.Hidden
import androidx.compose.material3.SheetValue.PartiallyExpanded
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme
import io.github.reactivecircus.kstreamlined.android.core.designsystem.preview.PreviewKStreamlined
import androidx.compose.material3.ModalBottomSheet as M3ModalBottomSheet
import androidx.compose.material3.SheetState as M3SheetState
import androidx.compose.material3.rememberBottomSheetState as rememberM3BottomSheetState

@Composable
public fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    color: Color = KSTheme.colorScheme.surface,
    contentColor: Color = KSTheme.colorScheme.onBackground,
    content: @Composable (ColumnScope.() -> Unit),
) {
    M3ModalBottomSheet(
        modifier = modifier,
        sheetState = sheetState.sheetState,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        containerColor = color,
        contentColor = contentColor,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                shape = CircleShape,
                color = KSTheme.colorScheme.onBackground,
            )
        },
        onDismissRequest = onDismissRequest,
        content = content,
    )
}

public class SheetState internal constructor(
    internal val sheetState: M3SheetState,
) {
    public val isVisible: Boolean
        get() = sheetState.isVisible

    public suspend fun show() {
        sheetState.show()
    }

    public suspend fun hide() {
        sheetState.hide()
    }
}

@Composable
public fun rememberModalBottomSheetState(skipPartiallyExpanded: Boolean = false): SheetState {
    val m3SheetState = rememberM3BottomSheetState(
        initialValue = Hidden,
        enabledValues = if (skipPartiallyExpanded) {
            setOf(Hidden, Expanded)
        } else {
            setOf(Hidden, PartiallyExpanded, Expanded)
        },
    )
    return remember(skipPartiallyExpanded) { SheetState(m3SheetState) }
}

@Composable
@PreviewKStreamlined
private fun PreviewModalBottomSheet() {
    val m3SheetState = rememberM3BottomSheetState(initialValue = Expanded)
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
