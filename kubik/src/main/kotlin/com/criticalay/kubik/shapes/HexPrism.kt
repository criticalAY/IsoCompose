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
