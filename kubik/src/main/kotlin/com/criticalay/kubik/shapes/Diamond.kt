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
