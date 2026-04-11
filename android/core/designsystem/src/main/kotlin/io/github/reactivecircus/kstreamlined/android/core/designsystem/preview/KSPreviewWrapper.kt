package io.github.reactivecircus.kstreamlined.android.core.designsystem.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewWrapperProvider
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

public class KSPreviewWrapper : PreviewWrapperProvider {
    @Composable
    override fun Wrap(content: @Composable (() -> Unit)) {
        KSTheme {
            Surface {
                content()
            }
        }
    }
}
