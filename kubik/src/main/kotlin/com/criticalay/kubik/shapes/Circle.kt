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
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A circular [Path] approximated by a regular polygon.
 */
class Circle(
    origin: Point,
    radius: Double = 1.0,
    vertices: Int = 20
) : Path(createCirclePoints(origin, radius, vertices)) {
    companion object {
        private fun createCirclePoints(origin: Point, radius: Double, vertices: Int): List<Point> {
            require(radius > 0.0) { "Circle radius must be positive" }
            require(vertices >= 3) { "Circle needs at least 3 vertices" }

            return List(vertices) { i ->
                Point(
                    radius * cos(i * 2 * PI / vertices) + origin.x,
                    radius * sin(i * 2 * PI / vertices) + origin.y,
                    origin.z
                )
            }
        }
    }
}
