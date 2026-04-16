package com.criticalay.kubik.compose

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.criticalay.kubik.core.Vector
import com.criticalay.kubik.engine.IsometricEngine
import com.criticalay.kubik.engine.RenderOptions
import kotlin.math.PI

/**
 * A Composable that renders an isometric 3D scene on a Compose Canvas.
 *
 * Usage:
 * ```
 * IsometricCanvas(modifier = Modifier.fillMaxSize()) {
 *     shape(Prism(Point(0.0, 0.0, 0.0), 2.0, 2.0, 1.0), Color.Blue)
 *     shape(Pyramid(Point(3.0, 0.0, 0.0)), Color.Red)
 * }
 * ```
 *
 * @param modifier Modifier for the Canvas
 * @param scale Pixels per world unit (default 70)
 * @param angle Isometric projection angle in radians (default 30 degrees)
 * @param lightDirection Direction of the light source
 * @param renderOptions Controls culling, depth sorting, etc.
 * @param content DSL block to declare shapes and paths
 */
@Composable
fun IsometricCanvas(
    modifier: Modifier = Modifier,
    scale: Double = 70.0,
    angle: Double = PI / 6,
    lightDirection: Vector = IsometricEngine.DEFAULT_LIGHT_DIRECTION,
    renderOptions: RenderOptions = RenderOptions.Default,
    content: SceneScope.() -> Unit
) {
    val engine = remember { IsometricEngine(angle, scale) }

    // Update engine if params change
    if (engine.angle != angle) engine.angle = angle
    if (engine.scale != scale) engine.scale = scale

    Canvas(modifier = modifier) {
        renderScene(engine, lightDirection, renderOptions, content)
    }
}

private fun DrawScope.renderScene(
    engine: IsometricEngine,
    lightDirection: Vector,
    renderOptions: RenderOptions,
    content: SceneScope.() -> Unit
) {
    val w = size.width.toInt()
    val h = size.height.toInt()
    if (w <= 0 || h <= 0) return

    engine.clear()
    SceneScope(engine).content()

    val scene = engine.projectScene(w, h, renderOptions, lightDirection.normalize())

    for (cmd in scene.commands) {
        val path = cmd.toComposePath()
        drawPath(path, color = cmd.color.toComposeColor())
    }
}
