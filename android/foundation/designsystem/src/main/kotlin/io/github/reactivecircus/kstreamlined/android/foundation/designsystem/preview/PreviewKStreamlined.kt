package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.component.Surface
import io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.KSTheme

@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
@PreviewLightDark
public annotation class PreviewKStreamlined

@Composable
public fun KSThemeWithSurface(
    content: @Composable () -> Unit
) {
    KSTheme {
        Surface {
            content()
        }
    }
}
