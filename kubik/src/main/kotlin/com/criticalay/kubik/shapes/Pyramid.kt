package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape
import kotlin.math.PI

/**
 * A pyramid with a rectangular base tapering to an apex.
 */
class Pyramid(
    val position: Point = Point.ORIGIN,
    val width: Double = 1.0,
    val depth: Double = 1.0,
    val height: Double = 1.0
) : Shape(createPaths(position, width, depth, height)) {
    init {
        require(width > 0.0) { "Pyramid width must be positive" }
        require(depth > 0.0) { "Pyramid depth must be positive" }
        require(height > 0.0) { "Pyramid height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Pyramid =
        Pyramid(position.translate(dx, dy, dz), width, depth, height)

    companion object {
        private fun createPaths(position: Point, width: Double, depth: Double, height: Double): List<Path> {
            val p = position
            val center = p.translate(width / 2.0, depth / 2.0, 0.0)
            val paths = mutableListOf<Path>()

            val face1 = Path(p, Point(p.x + width, p.y, p.z), Point(p.x + width / 2.0, p.y + depth / 2.0, p.z + height))
            paths.add(face1)
            paths.add(face1.rotateZ(center, PI))

            val face2 = Path(p, Point(p.x + width / 2.0, p.y + depth / 2.0, p.z + height), Point(p.x, p.y + depth, p.z))
            paths.add(face2)
            paths.add(face2.rotateZ(center, PI))

            return paths
        }
    }
}
