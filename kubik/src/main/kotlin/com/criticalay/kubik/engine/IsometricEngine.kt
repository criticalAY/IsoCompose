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
import com.criticalay.kubik.core.Shape
import com.criticalay.kubik.core.Vector
import kotlin.math.PI

/**
 * Core isometric rendering engine.
 * Platform-agnostic — outputs [PreparedScene] for any renderer.
 */
class IsometricEngine(
    angle: Double = PI / 6,
    scale: Double = 70.0,
    private val colorDifference: Double = 0.20,
    private val lightColor: IsoColor = IsoColor.WHITE
) {
    companion object {
        val DEFAULT_LIGHT_DIRECTION: Vector = Vector(2.0, -1.0, 3.0)
    }

    var angle: Double = angle
        set(value) {
            require(value.isFinite()) { "angle must be finite" }
            field = value
            rebuildProjection()
        }

    var scale: Double = scale
        set(value) {
            require(value.isFinite() && value > 0.0) { "scale must be positive and finite" }
            field = value
            rebuildProjection()
        }

    init {
        require(angle.isFinite()) { "angle must be finite" }
        require(scale.isFinite() && scale > 0.0) { "scale must be positive and finite" }
    }

    private val sceneGraph = SceneGraph()
    private var projection = IsometricProjection(angle, scale, colorDifference, lightColor)

    private fun rebuildProjection() {
        projection = IsometricProjection(this.angle, this.scale, colorDifference, lightColor)
    }

    fun worldToScreen(point: Point, viewportWidth: Int, viewportHeight: Int): Point2D {
        return projection.translatePoint(point, viewportWidth / 2.0, viewportHeight * 0.9)
    }

    fun screenToWorld(screenPoint: Point2D, viewportWidth: Int, viewportHeight: Int, z: Double = 0.0): Point {
        return projection.screenToWorld(screenPoint, viewportWidth / 2.0, viewportHeight * 0.9, z)
    }

    fun add(shape: Shape, color: IsoColor) = sceneGraph.add(shape, color)

    fun add(path: Path, color: IsoColor, originalShape: Shape? = null, id: String? = null) {
        sceneGraph.add(path, color, originalShape, id)
    }

    fun clear() = sceneGraph.clear()

    fun projectScene(
        width: Int,
        height: Int,
        renderOptions: RenderOptions = RenderOptions.Default,
        lightDirection: Vector = DEFAULT_LIGHT_DIRECTION.normalize()
    ): PreparedScene {
        val normalizedLight = lightDirection.normalize()
        val originX = width / 2.0
        val originY = height * 0.9

        val transformedItems = sceneGraph.items.mapNotNull { item ->
            projectAndCull(item, originX, originY, renderOptions, normalizedLight, width, height)
        }

        val sortedItems = if (renderOptions.enableDepthSorting) {
            DepthSorter.sort(transformedItems, renderOptions)
        } else {
            transformedItems
        }

        val commands = sortedItems.map { transformed ->
            RenderCommand(
                commandId = transformed.item.id,
                points = transformed.transformedPoints,
                color = transformed.litColor,
                originalPath = transformed.item.path,
                originalShape = transformed.item.originalShape
            )
        }

        return PreparedScene(commands, width, height)
    }

    fun findItemAt(
        preparedScene: PreparedScene,
        x: Double,
        y: Double,
        order: HitOrder = HitOrder.FRONT_TO_BACK,
        touchRadius: Double = 0.0
    ): RenderCommand? = HitTester.findItemAt(preparedScene, x, y, order, touchRadius)

    private fun projectAndCull(
        item: SceneGraph.SceneItem,
        originX: Double, originY: Double,
        renderOptions: RenderOptions,
        normalizedLight: Vector,
        width: Int, height: Int
    ): DepthSorter.TransformedItem? {
        val screenPoints = item.path.points.map { projection.translatePoint(it, originX, originY) }

        if (renderOptions.enableBackfaceCulling && projection.cullPath(screenPoints)) return null
        if (renderOptions.enableBoundsChecking && !projection.itemInDrawingBounds(screenPoints, width, height)) return null

        val litColor = projection.transformColor(item.path, item.baseColor, normalizedLight)
        return DepthSorter.TransformedItem(item, screenPoints, litColor)
    }
}
