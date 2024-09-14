package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Copied from `androidx.compose.material.icons.rounded.Bookmarks`.
 */
public val KSIcons.Bookmarks: ImageVector
    get() {
        if (_bookmarks != null) {
            return _bookmarks!!
        }
        _bookmarks = materialIcon(name = "Rounded.Bookmarks") {
            materialPath {
                moveTo(19.0f, 18.0f)
                lineToRelative(2.0f, 1.0f)
                verticalLineTo(3.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                horizontalLineTo(8.99f)
                curveTo(7.89f, 1.0f, 7.0f, 1.9f, 7.0f, 3.0f)
                horizontalLineToRelative(10.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, 0.9f, 2.0f, 2.0f)
                verticalLineToRelative(13.0f)
                close()
                moveTo(15.0f, 5.0f)
                horizontalLineTo(5.0f)
                curveToRelative(-1.1f, 0.0f, -2.0f, 0.9f, -2.0f, 2.0f)
                verticalLineToRelative(16.0f)
                lineToRelative(7.0f, -3.0f)
                lineToRelative(7.0f, 3.0f)
                verticalLineTo(7.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                close()
            }
        }
        return _bookmarks!!
    }

private var _bookmarks: ImageVector? = null
