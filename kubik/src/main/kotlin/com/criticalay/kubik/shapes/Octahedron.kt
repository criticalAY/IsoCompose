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
import kotlin.math.sqrt

/**
 * An octahedron (8-faced polyhedron) inscribed in a unit cube.
 */
class Octahedron(val position: Point = Point.ORIGIN) : Shape(createPaths(position)) {

    override fun translate(dx: Double, dy: Double, dz: Double): Octahedron =
        Octahedron(position.translate(dx, dy, dz))

    companion object {
        private fun createPaths(position: Point): List<Path> {
            val center = position.translate(0.5, 0.5, 0.5)
            val upper = Path(
                position.translate(0.0, 0.0, 0.5),
                position.translate(0.5, 0.5, 1.0),
                position.translate(0.0, 1.0, 0.5)
            )
            val lower = Path(
                position.translate(0.0, 0.0, 0.5),
                position.translate(0.0, 1.0, 0.5),
                position.translate(0.5, 0.5, 0.0)
            )

            val paths = mutableListOf<Path>()
            for (i in 0 until 4) {
                paths.add(upper.rotateZ(center, i * PI / 2.0))
                paths.add(lower.rotateZ(center, i * PI / 2.0))
            }

            val s = sqrt(2.0) / 2.0
            return paths.map { it.scale(center, s, s, 1.0) }
        }
    }
}
