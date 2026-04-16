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
