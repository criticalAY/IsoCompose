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
