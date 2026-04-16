package com.criticalay.kubik.core

import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * A point in 3D isometric space.
 *
 * Coordinate system:
 * - [x]: right-and-down on screen (toward bottom-right)
 * - [y]: left-and-down on screen (toward bottom-left)
 * - [z]: straight up on screen (upward)
 */
class Point(
    val x: Double = 0.0,
    val y: Double = 0.0,
    val z: Double = 0.0
) {
    companion object {
        val ORIGIN = Point(0.0, 0.0, 0.0)

        fun distance(p1: Point, p2: Point): Double = sqrt(distance2(p1, p2))

        fun distance2(p1: Point, p2: Point): Double {
            val dx = p2.x - p1.x
            val dy = p2.y - p1.y
            val dz = p2.z - p1.z
            return dx * dx + dy * dy + dz * dz
        }

        fun distanceToSegmentSquared(p: Point, v: Point, w: Point): Double {
            val l2 = distance2(v, w)
            if (l2 == 0.0) return distance2(p, v)
            val t = ((p.x - v.x) * (w.x - v.x) + (p.y - v.y) * (w.y - v.y)) / l2
            if (t < 0) return distance2(p, v)
            if (t > 1) return distance2(p, w)
            return distance2(p, Point(v.x + t * (w.x - v.x), v.y + t * (w.y - v.y)))
        }

        fun distanceToSegment(p: Point, v: Point, w: Point): Double {
            return sqrt(distanceToSegmentSquared(p, v, w))
        }
    }

    fun translate(dx: Double, dy: Double, dz: Double): Point {
        return Point(x + dx, y + dy, z + dz)
    }

    operator fun plus(other: Point): Point = Point(x + other.x, y + other.y, z + other.z)
    operator fun plus(vector: Vector): Point = Point(x + vector.x, y + vector.y, z + vector.z)
    operator fun minus(other: Point): Vector = Vector(x - other.x, y - other.y, z - other.z)
    operator fun minus(vector: Vector): Point = Point(x - vector.x, y - vector.y, z - vector.z)
    operator fun times(scalar: Double): Point = Point(x * scalar, y * scalar, z * scalar)
    operator fun unaryMinus(): Point = Point(-x, -y, -z)

    fun scale(origin: Point, dx: Double, dy: Double, dz: Double): Point {
        return Point(
            (x - origin.x) * dx + origin.x,
            (y - origin.y) * dy + origin.y,
            (z - origin.z) * dz + origin.z
        )
    }

    fun scale(origin: Point, dx: Double): Point = scale(origin, dx, dx, dx)
    fun scale(origin: Point, dx: Double, dy: Double): Point = scale(origin, dx, dy, 1.0)

    fun rotateX(origin: Point, angle: Double): Point {
        val pY = y - origin.y
        val pZ = z - origin.z
        val cosA = cos(angle)
        val sinA = sin(angle)
        return Point(x, pZ * sinA + pY * cosA + origin.y, pZ * cosA - pY * sinA + origin.z)
    }

    fun rotateY(origin: Point, angle: Double): Point {
        val pX = x - origin.x
        val pZ = z - origin.z
        val cosA = cos(angle)
        val sinA = sin(angle)
        return Point(pX * cosA - pZ * sinA + origin.x, y, pX * sinA + pZ * cosA + origin.z)
    }

    fun rotateZ(origin: Point, angle: Double): Point {
        val pX = x - origin.x
        val pY = y - origin.y
        val cosA = cos(angle)
        val sinA = sin(angle)
        return Point(pX * cosA - pY * sinA + origin.x, pX * sinA + pY * cosA + origin.y, z)
    }

    /**
     * Depth for painter's algorithm: higher = farther from viewer.
     */
    fun depth(): Double = x + y - 2 * z

    /**
     * Depth for an arbitrary projection angle.
     */
    fun depth(angle: Double): Double = x * cos(angle) + y * sin(angle) - 2 * z

    override fun equals(other: Any?): Boolean =
        other is Point && x == other.x && y == other.y && z == other.z

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String = "Point(x=$x, y=$y, z=$z)"
}
