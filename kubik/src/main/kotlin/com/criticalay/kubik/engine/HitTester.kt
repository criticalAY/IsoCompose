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

import com.criticalay.kubik.core.Point

/**
 * Hit testing: find the render command at a screen coordinate.
 */
internal object HitTester {

    fun findItemAt(
        preparedScene: PreparedScene,
        x: Double, y: Double,
        order: HitOrder = HitOrder.FRONT_TO_BACK,
        touchRadius: Double = 0.0
    ): RenderCommand? {
        val commands = when (order) {
            HitOrder.FRONT_TO_BACK -> preparedScene.commands.reversed()
            HitOrder.BACK_TO_FRONT -> preparedScene.commands
        }

        for (cmd in commands) {
            val points = cmd.points.map { Point(it.x, it.y, 0.0) }
            val hit = if (touchRadius > 0.0) {
                IntersectionUtils.isPointCloseToPoly(points, x, y, touchRadius) ||
                    IntersectionUtils.isPointInPoly(points, x, y)
            } else {
                IntersectionUtils.isPointInPoly(points, x, y)
            }
            if (hit) return cmd
        }
        return null
    }
}
