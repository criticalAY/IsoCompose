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

package com.criticalay.kubik.core

import kotlin.math.sqrt

/**
 * A three-dimensional vector for directions, normals, and light calculations.
 */
class Vector(
    val x: Double,
    val y: Double,
    val z: Double
) {
    companion object {
        fun fromTwoPoints(p1: Point, p2: Point): Vector {
            return Vector(p2.x - p1.x, p2.y - p1.y, p2.z - p1.z)
        }

        fun crossProduct(v1: Vector, v2: Vector): Vector {
            return Vector(
                v1.y * v2.z - v2.y * v1.z,
                -(v1.x * v2.z - v2.x * v1.z),
                v1.x * v2.y - v2.x * v1.y
            )
        }

        fun dotProduct(v1: Vector, v2: Vector): Double {
            return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z
        }
    }

    fun magnitude(): Double = sqrt(x * x + y * y + z * z)

    fun normalize(): Vector {
        val mag = magnitude()
        if (mag == 0.0) return Vector(0.0, 0.0, 0.0)
        return Vector(x / mag, y / mag, z / mag)
    }

    operator fun plus(other: Vector): Vector = Vector(x + other.x, y + other.y, z + other.z)
    operator fun minus(other: Vector): Vector = Vector(x - other.x, y - other.y, z - other.z)
    operator fun times(scalar: Double): Vector = Vector(x * scalar, y * scalar, z * scalar)
    operator fun unaryMinus(): Vector = Vector(-x, -y, -z)

    infix fun cross(other: Vector): Vector = Vector(
        y * other.z - z * other.y,
        z * other.x - x * other.z,
        x * other.y - y * other.x
    )

    infix fun dot(other: Vector): Double = x * other.x + y * other.y + z * other.z

    override fun equals(other: Any?): Boolean =
        other is Vector && x == other.x && y == other.y && z == other.z

    override fun hashCode(): Int {
        var result = x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        return result
    }

    override fun toString(): String = "Vector(x=$x, y=$y, z=$z)"
}
