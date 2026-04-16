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

package com.criticalay.kubik.engine

import com.criticalay.kubik.core.IsoColor
import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Point2D
import com.criticalay.kubik.core.Vector
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Pure projection logic: 3D points to 2D screen coordinates,
 * lighting, back-face culling, and bounds checking.
 */
internal class IsometricProjection(
    angle: Double,
    private val scale: Double,
    private val colorDifference: Double,
    private val lightColor: IsoColor
) {
    private val transformation: Array<DoubleArray> = arrayOf(
        doubleArrayOf(scale * cos(angle), scale * sin(angle)),
        doubleArrayOf(scale * cos(PI - angle), scale * sin(PI - angle))
    )

    /** Project a 3D point to 2D screen coordinates. */
    fun translatePoint(point: Point, originX: Double, originY: Double): Point2D {
        return Point2D(
            originX + point.x * transformation[0][0] + point.y * transformation[1][0],
            originY - point.x * transformation[0][1] - point.y * transformation[1][1] - (point.z * scale)
        )
    }

    /** Unproject a 2D screen point back to 3D world on a given Z plane. */
    fun screenToWorld(screenPoint: Point2D, originX: Double, originY: Double, z: Double): Point {
        val a = transformation[0][0]
        val b = transformation[1][0]
        val c = transformation[0][1]
        val d = transformation[1][1]

        val rhs1 = screenPoint.x - originX
        val rhs2 = originY - screenPoint.y - z * scale

        val det = a * d - b * c
        require(kotlin.math.abs(det) > 1e-10) { "Near-degenerate projection matrix" }

        val worldX = (rhs1 * d - rhs2 * b) / det
        val worldY = (rhs2 * a - rhs1 * c) / det

        return Point(worldX, worldY, z)
    }

    /** Apply lighting based on surface normal. */
    fun transformColor(path: Path, color: IsoColor, lightDirection: Vector): IsoColor {
        if (path.points.size < 3) return color

        val edge1 = Vector.fromTwoPoints(path.points[1], path.points[0])
        val edge2 = Vector.fromTwoPoints(path.points[2], path.points[1])

        val normal = (edge1 cross edge2).normalize()
        val brightness = normal dot lightDirection

        return color.lighten(brightness * colorDifference, lightColor)
    }

    /** Back-face culling: returns true if path faces away from viewer. */
    fun cullPath(transformedPoints: List<Point2D>): Boolean {
        if (transformedPoints.size < 3) return false

        val a = transformedPoints[0].x * transformedPoints[1].y
        val b = transformedPoints[1].x * transformedPoints[2].y
        val c = transformedPoints[2].x * transformedPoints[0].y

        val d = transformedPoints[1].x * transformedPoints[0].y
        val e = transformedPoints[2].x * transformedPoints[1].y
        val f = transformedPoints[0].x * transformedPoints[2].y

        return (a + b + c - d - e - f) > 0
    }

    /** Check if any point is within viewport bounds. */
    fun itemInDrawingBounds(transformedPoints: List<Point2D>, width: Int, height: Int): Boolean {
        for (point in transformedPoints) {
            if (point.x >= 0 && point.x <= width && point.y >= 0 && point.y <= height) {
                return true
            }
        }
        return false
    }
}
