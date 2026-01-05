package io.github.reactivecircus.kstreamlined.android.core.designsystem.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import io.github.reactivecircus.kstreamlined.android.core.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.core.designsystem.foundation.KSTheme

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PreviewLightDark
public annotation class PreviewKStreamlined

@Composable
public fun KSThemeWithSurface(
    content: @Composable () -> Unit,
) {
    KSTheme {
        Surface {
            content()
        }
    }
}
