package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape

/**
 * A rectangular prism (box) with six quad faces.
 */
class Prism(
    val position: Point = Point.ORIGIN,
    val width: Double = 1.0,
    val depth: Double = 1.0,
    val height: Double = 1.0
) : Shape(createPaths(position, width, depth, height)) {
    init {
        require(width > 0.0) { "Prism width must be positive" }
        require(depth > 0.0) { "Prism depth must be positive" }
        require(height > 0.0) { "Prism height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Prism =
        Prism(position.translate(dx, dy, dz), width, depth, height)

    companion object {
        private fun createPaths(position: Point, width: Double, depth: Double, height: Double): List<Path> {
            val p = position
            val paths = mutableListOf<Path>()

            // Face parallel to x-axis
            val face1 = Path(p, Point(p.x + width, p.y, p.z), Point(p.x + width, p.y, p.z + height), Point(p.x, p.y, p.z + height))
            paths.add(face1)
            paths.add(face1.reverse().translate(0.0, depth, 0.0))

            // Face parallel to y-axis
            val face2 = Path(p, Point(p.x, p.y, p.z + height), Point(p.x, p.y + depth, p.z + height), Point(p.x, p.y + depth, p.z))
            paths.add(face2)
            paths.add(face2.reverse().translate(width, 0.0, 0.0))

            // Top and bottom (xy-plane)
            val face3 = Path(p, Point(p.x + width, p.y, p.z), Point(p.x + width, p.y + depth, p.z), Point(p.x, p.y + depth, p.z))
            paths.add(face3.reverse())
            paths.add(face3.translate(0.0, 0.0, height))

            return paths
        }
    }
}
