package com.criticalay.kubik.engine

import com.criticalay.kubik.core.IsoColor
import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point2D
import com.criticalay.kubik.core.Shape

/**
 * A platform-agnostic rendering command for a single polygon.
 * Contains 2D screen-space points and lit color, ready for rendering.
 */
class RenderCommand(
    val commandId: String,
    val points: List<Point2D>,
    val color: IsoColor,
    val originalPath: Path,
    val originalShape: Shape?
)
