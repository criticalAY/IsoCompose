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
