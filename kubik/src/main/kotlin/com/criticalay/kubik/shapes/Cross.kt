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
 * A plus/cross shape extruded along the z-axis.
 *
 * The cross is centered at [position] with total extent of [size] on both axes.
 * The arm width is controlled by [armWidth].
 *
 * @param position Center of the cross base
 * @param size Total extent on x and y axes (must be positive, default 2.0)
 * @param armWidth Width of each arm (must be positive and less than size, default 0.6)
 * @param height Extrusion height along z-axis (must be positive, default 0.5)
 */
class Cross(
    val position: Point = Point.ORIGIN,
    val size: Double = 2.0,
    val armWidth: Double = 0.6,
    val height: Double = 0.5
) : Shape(createCross(position, size, armWidth, height)) {
    init {
        require(size > 0.0) { "Cross size must be positive" }
        require(armWidth > 0.0) { "Cross armWidth must be positive" }
        require(armWidth < size) { "armWidth must be less than size" }
        require(height > 0.0) { "Cross height must be positive" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Cross =
        Cross(position.translate(dx, dy, dz), size, armWidth, height)

    companion object {
        private fun createCross(position: Point, size: Double, armWidth: Double, height: Double): List<Path> {
            val half = size / 2.0
            val hw = armWidth / 2.0
            val px = position.x
            val py = position.y
            val pz = position.z

            // 12-point plus shape, going clockwise from top-right of top arm
            val crossPoints = listOf(
                Point(px + hw, py - half, pz),  // top arm, right edge
                Point(px + hw, py - hw, pz),    // inner top-right
                Point(px + half, py - hw, pz),  // right arm, top edge
                Point(px + half, py + hw, pz),  // right arm, bottom edge
                Point(px + hw, py + hw, pz),    // inner bottom-right
                Point(px + hw, py + half, pz),  // bottom arm, right edge
                Point(px - hw, py + half, pz),  // bottom arm, left edge
                Point(px - hw, py + hw, pz),    // inner bottom-left
                Point(px - half, py + hw, pz),  // left arm, bottom edge
                Point(px - half, py - hw, pz),  // left arm, top edge
                Point(px - hw, py - hw, pz),    // inner top-left
                Point(px - hw, py - half, pz)   // top arm, left edge
            )

            val crossPath = Path(crossPoints)
            return Shape.extrude(crossPath, height).paths
        }
    }
}
