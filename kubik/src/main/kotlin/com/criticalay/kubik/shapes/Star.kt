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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A 5-pointed star extruded along the z-axis.
 *
 * @param position Center of the star base
 * @param outerRadius Radius to the star points (must be positive, default 1.0)
 * @param innerRadius Radius to the inner notches (must be positive, default 0.4)
 * @param height Extrusion height along z-axis (must be positive, default 0.5)
 * @param points Number of star points (minimum 3, default 5)
 */
class Star(
    val position: Point = Point.ORIGIN,
    val outerRadius: Double = 1.0,
    val innerRadius: Double = 0.4,
    val height: Double = 0.5,
    val points: Int = 5
) : Shape(createStar(position, outerRadius, innerRadius, height, points)) {
    init {
        require(outerRadius > 0.0) { "Star outerRadius must be positive" }
        require(innerRadius > 0.0) { "Star innerRadius must be positive" }
        require(innerRadius < outerRadius) { "innerRadius must be less than outerRadius" }
        require(height > 0.0) { "Star height must be positive" }
        require(points >= 3) { "Star needs at least 3 points" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Star =
        Star(position.translate(dx, dy, dz), outerRadius, innerRadius, height, points)

    companion object {
        private fun createStar(
            position: Point, outerRadius: Double, innerRadius: Double,
            height: Double, points: Int
        ): List<Path> {
            val totalVertices = points * 2
            val starPoints = List(totalVertices) { i ->
                val angle = i * PI / points - PI / 2 // Start from top
                val radius = if (i % 2 == 0) outerRadius else innerRadius
                Point(
                    radius * cos(angle) + position.x,
                    radius * sin(angle) + position.y,
                    position.z
                )
            }

            val starPath = Path(starPoints)
            return Shape.extrude(starPath, height).paths
        }
    }
}
