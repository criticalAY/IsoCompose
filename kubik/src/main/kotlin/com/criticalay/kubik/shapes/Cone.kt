package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A cone with a circular base tapering to an apex.
 *
 * @param position Center of the base circle
 * @param radius Radius of the base (must be positive, default 1.0)
 * @param height Height along the z-axis (must be positive, default 1.0)
 * @param vertices Number of sides approximating the circle (minimum 3, default 20)
 */
class Cone(
    val position: Point = Point.ORIGIN,
    val radius: Double = 1.0,
    val height: Double = 1.0,
    val vertices: Int = 20
) : Shape(createPaths(position, radius, height, vertices)) {
    init {
        require(radius > 0.0) { "Cone radius must be positive" }
        require(height > 0.0) { "Cone height must be positive" }
        require(vertices >= 3) { "Cone needs at least 3 vertices" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Cone =
        Cone(position.translate(dx, dy, dz), radius, height, vertices)

    companion object {
        private fun createPaths(position: Point, radius: Double, height: Double, vertices: Int): List<Path> {
            val paths = mutableListOf<Path>()
            val apex = Point(position.x, position.y, position.z + height)

            // Generate base circle points
            val basePoints = List(vertices) { i ->
                Point(
                    radius * cos(i * 2 * PI / vertices) + position.x,
                    radius * sin(i * 2 * PI / vertices) + position.y,
                    position.z
                )
            }

            // Base face (reversed for correct winding)
            paths.add(Path(basePoints.reversed()))

            // Triangular side faces from each base edge to the apex
            for (i in 0 until vertices) {
                val next = (i + 1) % vertices
                paths.add(Path(basePoints[i], basePoints[next], apex))
            }

            return paths
        }
    }
}
