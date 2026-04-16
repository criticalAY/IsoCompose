/*
 * Copyright 2026 Ashish Yadav <mailtoashish693@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape

/**
 * A triangular wedge (ramp/slope) shape with 5 faces.
 *
 * The wedge sits with its base on the xy-plane. The front face (at y=0) is a
 * full rectangle of width × height, and the shape slopes down to zero height
 * at y=depth.
 *
 * @param position The minimum-corner origin point
 * @param width Extent along the x-axis (must be positive, default 1.0)
 * @param depth Extent along the y-axis (must be positive, default 1.0)
 * @param height Extent along the z-axis at the front face (must be positive, default 1.0)
 */
class Wedge(
    val position: Point = Point.ORIGIN,
    val width: Double = 1.0,
    val depth: Double = 1.0,
    val height: Double = 1.0
) : Shape(createPaths(position, width, depth, height)) {
    init {
        require(width > 0.0) { "Wedge width must be positive" }
        require(depth > 0.0) { "Wedge depth must be positive" }
        require(height > 0.0) { "Wedge height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Wedge =
        Wedge(position.translate(dx, dy, dz), width, depth, height)

    companion object {
        private fun createPaths(position: Point, width: Double, depth: Double, height: Double): List<Path> {
            val p = position

            // 6 key vertices
            val p0 = p                                                    // front-bottom-left
            val p1 = Point(p.x + width, p.y, p.z)                       // front-bottom-right
            val p2 = Point(p.x + width, p.y, p.z + height)              // front-top-right
            val p3 = Point(p.x, p.y, p.z + height)                      // front-top-left
            val p4 = Point(p.x, p.y + depth, p.z)                       // back-bottom-left
            val p5 = Point(p.x + width, p.y + depth, p.z)               // back-bottom-right

            return listOf(
                // Front face (rectangle)
                Path(p0, p1, p2, p3),
                // Bottom face (rectangle)
                Path(p0, p4, p5, p1).reverse(),
                // Back face (just the bottom edge — height is 0 at back, so no back wall)
                // Left triangle side
                Path(p0, p3, p4),
                // Right triangle side
                Path(p1, p5, p2),
                // Sloped top face (rectangle from front-top to back-bottom)
                Path(p3, p2, p5, p4)
            )
        }
    }
}
