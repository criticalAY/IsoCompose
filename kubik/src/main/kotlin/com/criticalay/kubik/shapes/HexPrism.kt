package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape

/**
 * A hexagonal prism — a 6-sided column.
 * Created by extruding a regular hexagon along the z-axis.
 *
 * @param position Center of the base hexagon
 * @param radius Radius of the hexagon (must be positive, default 1.0)
 * @param height Height along the z-axis (must be positive, default 1.0)
 */
class HexPrism(
    val position: Point = Point.ORIGIN,
    val radius: Double = 1.0,
    val height: Double = 1.0
) : Shape(Shape.extrude(Circle(position, radius, vertices = 6), height).paths) {
    init {
        require(radius > 0.0) { "HexPrism radius must be positive" }
        require(height > 0.0) { "HexPrism height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): HexPrism =
        HexPrism(position.translate(dx, dy, dz), radius, height)
}
