package com.criticalay.kubik.compose

import androidx.compose.ui.graphics.Path
import com.criticalay.kubik.engine.RenderCommand

/** Convert a [RenderCommand]'s 2D points to a Compose [Path]. */
fun RenderCommand.toComposePath(): Path {
    val path = Path()
    if (points.isEmpty()) return path

    path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
    for (i in 1 until points.size) {
        path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
    }
    path.close()
    return path
}
