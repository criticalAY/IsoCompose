package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape

/**
 * A cylinder created by extruding a circular cross-section along the z-axis.
 */
class Cylinder(
    val position: Point = Point.ORIGIN,
    val radius: Double = 1.0,
    val height: Double = 1.0,
    val vertices: Int = 20
) : Shape(Shape.extrude(Circle(position, radius, vertices), height).paths) {
    init {
        require(radius > 0.0) { "Cylinder radius must be positive" }
        require(height > 0.0) { "Cylinder height must be positive" }
        require(vertices >= 3) { "Cylinder needs at least 3 vertices" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Cylinder =
        Cylinder(position.translate(dx, dy, dz), radius, height, vertices)
}
