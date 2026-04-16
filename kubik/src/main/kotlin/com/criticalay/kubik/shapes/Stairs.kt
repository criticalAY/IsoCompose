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
 * A staircase shape with steps distributed within a 1x1x1 bounding box.
 */
class Stairs(
    val position: Point = Point.ORIGIN,
    val stepCount: Int
) : Shape(createPaths(position, stepCount)) {
    init {
        require(stepCount >= 1) { "Stairs needs at least 1 step" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Stairs =
        Stairs(position.translate(dx, dy, dz), stepCount)

    companion object {
        private fun createPaths(position: Point, stepCount: Int): List<Path> {
            val paths = mutableListOf<Path>()
            val zigzag = mutableListOf<Point>()

            zigzag.add(position)

            for (i in 0 until stepCount) {
                val sc = position.translate(0.0, i / stepCount.toDouble(), (i + 1) / stepCount.toDouble())

                // Vertical face
                paths.add(Path(sc, sc.translate(0.0, 0.0, -1.0 / stepCount), sc.translate(1.0, 0.0, -1.0 / stepCount), sc.translate(1.0, 0.0, 0.0)))
                zigzag.add(sc)

                // Horizontal face
                paths.add(Path(sc, sc.translate(1.0, 0.0, 0.0), sc.translate(1.0, 1.0 / stepCount, 0.0), sc.translate(0.0, 1.0 / stepCount, 0.0)))
                zigzag.add(sc.translate(0.0, 1.0 / stepCount, 0.0))
            }

            zigzag.add(position.translate(0.0, 1.0, 0.0))

            paths.add(Path(zigzag))
            paths.add(Path(zigzag.reversed()).translate(1.0, 0.0, 0.0))

            return paths
        }
    }
}
