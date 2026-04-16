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

/**
 * A 3D shape composed of multiple polygon faces ([Path] instances).
 * Immutable — every transform returns a new [Shape].
 *
 * Use [extrude] to create a 3D shape from a 2D [Path].
 */
open class Shape(
    val paths: List<Path>
) {
    init {
        require(paths.isNotEmpty()) { "Shape requires at least one path" }
    }

    constructor(vararg paths: Path) : this(paths.toList())

    open fun translate(dx: Double, dy: Double, dz: Double): Shape {
        return Shape(paths.map { it.translate(dx, dy, dz) })
    }

    fun rotateX(origin: Point, angle: Double): Shape = Shape(paths.map { it.rotateX(origin, angle) })
    fun rotateY(origin: Point, angle: Double): Shape = Shape(paths.map { it.rotateY(origin, angle) })
    fun rotateZ(origin: Point, angle: Double): Shape = Shape(paths.map { it.rotateZ(origin, angle) })

    fun scale(origin: Point, dx: Double, dy: Double, dz: Double): Shape {
        return Shape(paths.map { it.scale(origin, dx, dy, dz) })
    }

    fun scale(origin: Point, dx: Double, dy: Double): Shape = Shape(paths.map { it.scale(origin, dx, dy) })
    fun scale(origin: Point, dx: Double): Shape = Shape(paths.map { it.scale(origin, dx) })

    fun orderedPaths(): List<Path> = paths.sortedByDescending { it.depth }

    companion object {
        /**
         * Extrudes a 2D [Path] along the z-axis to create a 3D shape.
         * Creates bottom face, top face, and one side face per edge.
         */
        fun extrude(path: Path, height: Double = 1.0): Shape {
            val topPath = path.translate(0.0, 0.0, height)
            val length = path.points.size
            val allPaths = mutableListOf<Path>()

            allPaths.add(path.reverse())
            allPaths.add(topPath)

            for (i in 0 until length) {
                allPaths.add(
                    Path(
                        listOf(
                            topPath.points[i],
                            path.points[i],
                            path.points[(i + 1) % length],
                            topPath.points[(i + 1) % length]
                        )
                    )
                )
            }

            return Shape(allPaths)
        }
    }
}
