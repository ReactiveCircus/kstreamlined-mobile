package io.github.reactivecircus.kstreamlined.android.designsystem.foundation.icon

import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector

public val KSIcons.Kotlin: ImageVector
    get() {
        if (_kotlin != null) {
            return _kotlin!!
        }
        _kotlin = ksIcon(
            name = "Kotlin",
            pathData = listOf(
                PathData(
                    "M22 22H2V2H22L12 12L22 22Z",
                    DefaultFillType,
                ),
            )
        )
        return _kotlin!!
    }

private var _kotlin: ImageVector? = null
