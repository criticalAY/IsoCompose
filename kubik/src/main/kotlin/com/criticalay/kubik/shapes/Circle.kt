package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A circular [Path] approximated by a regular polygon.
 */
class Circle(
    origin: Point,
    radius: Double = 1.0,
    vertices: Int = 20
) : Path(createCirclePoints(origin, radius, vertices)) {
    companion object {
        private fun createCirclePoints(origin: Point, radius: Double, vertices: Int): List<Point> {
            require(radius > 0.0) { "Circle radius must be positive" }
            require(vertices >= 3) { "Circle needs at least 3 vertices" }

            return List(vertices) { i ->
                Point(
                    radius * cos(i * 2 * PI / vertices) + origin.x,
                    radius * sin(i * 2 * PI / vertices) + origin.y,
                    origin.z
                )
            }
        }
    }
}
