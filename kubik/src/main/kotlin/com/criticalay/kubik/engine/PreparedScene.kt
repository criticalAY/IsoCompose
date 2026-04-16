package com.criticalay.kubik.engine

/**
 * A prepared scene ready for rendering.
 * Contains render commands sorted back-to-front for correct depth ordering.
 */
class PreparedScene(
    val commands: List<RenderCommand>,
    val width: Int,
    val height: Int
)
