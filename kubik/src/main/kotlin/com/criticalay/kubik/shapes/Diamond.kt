package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape

/**
 * A diamond (rhombus) shape extruded along the z-axis.
 *
 * @param position Center of the diamond base
 * @param width Extent along the x-axis (must be positive, default 1.0)
 * @param depth Extent along the y-axis (must be positive, default 1.0)
 * @param height Extrusion height along z-axis (must be positive, default 0.5)
 */
class Diamond(
    val position: Point = Point.ORIGIN,
    val width: Double = 1.0,
    val depth: Double = 1.0,
    val height: Double = 0.5
) : Shape(createDiamond(position, width, depth, height)) {
    init {
        require(width > 0.0) { "Diamond width must be positive" }
        require(depth > 0.0) { "Diamond depth must be positive" }
        require(height > 0.0) { "Diamond height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Diamond =
        Diamond(position.translate(dx, dy, dz), width, depth, height)

    companion object {
        private fun createDiamond(position: Point, width: Double, depth: Double, height: Double): List<Path> {
            val hw = width / 2.0
            val hd = depth / 2.0

            val diamondPoints = listOf(
                Point(position.x + hw, position.y, position.z),       // right
                Point(position.x, position.y + hd, position.z),       // front
                Point(position.x - hw, position.y, position.z),       // left
                Point(position.x, position.y - hd, position.z)        // back
            )

            val diamondPath = Path(diamondPoints)
            return Shape.extrude(diamondPath, height).paths
        }
    }
}
