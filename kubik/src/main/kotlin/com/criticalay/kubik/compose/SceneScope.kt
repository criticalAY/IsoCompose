package com.criticalay.kubik.compose

import androidx.compose.ui.graphics.Color
import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Shape
import com.criticalay.kubik.engine.IsometricEngine

/**
 * DSL scope for building an isometric scene.
 * Used inside [IsometricCanvas] to declare shapes and paths.
 */
class SceneScope internal constructor(
    internal val engine: IsometricEngine
) {
    /** Add a 3D shape with a Compose color. */
    fun shape(shape: Shape, color: Color) {
        engine.add(shape, color.toIsoColor())
    }

    /** Add a raw 3D polygon face with a Compose color. */
    fun path(path: Path, color: Color) {
        engine.add(path, color.toIsoColor())
    }
}
