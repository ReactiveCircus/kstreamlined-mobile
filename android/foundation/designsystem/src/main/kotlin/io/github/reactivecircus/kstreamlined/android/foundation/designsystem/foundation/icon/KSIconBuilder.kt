package io.github.reactivecircus.kstreamlined.android.foundation.designsystem.foundation.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

internal fun ksIcon(
    name: String,
    pathData: List<PathData>,
) = ImageVector.Builder(
    name = name,
    defaultWidth = IconSize.dp,
    defaultHeight = IconSize.dp,
    viewportWidth = IconSize,
    viewportHeight = IconSize,
).apply {
    pathData.forEach {
        addPath(
            pathData = addPathNodes(it.path),
            pathFillType = it.fillType,
            fill = SolidColor(Color.Black),
            stroke = null,
            strokeLineWidth = 1f,
            strokeLineCap = StrokeCap.Butt,
            strokeLineJoin = StrokeJoin.Bevel,
            strokeLineMiter = 1f,
        )
    }
}.build()

internal class PathData(val path: String, val fillType: PathFillType)

private const val IconSize = 24f
