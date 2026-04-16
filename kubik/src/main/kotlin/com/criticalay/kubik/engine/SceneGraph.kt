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
import com.criticalay.kubik.core.Shape

/**
 * Mutable collection of scene items (paths with colors and metadata).
 */
internal class SceneGraph {
    internal data class SceneItem(
        val path: Path,
        val baseColor: IsoColor,
        val originalShape: Shape?,
        val id: String
    )

    private val _items = mutableListOf<SceneItem>()
    private var nextId = 0

    val items: List<SceneItem> get() = _items

    fun add(shape: Shape, color: IsoColor) {
        for (path in shape.orderedPaths()) {
            add(path, color, shape)
        }
    }

    fun add(path: Path, color: IsoColor, originalShape: Shape? = null, id: String? = null) {
        _items.add(SceneItem(path, color, originalShape, id ?: "item_${nextId++}"))
    }

    fun clear() {
        _items.clear()
        nextId = 0
    }
}
