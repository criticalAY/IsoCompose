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
import com.criticalay.kubik.core.Point2D
import com.criticalay.kubik.core.Shape

/**
 * A platform-agnostic rendering command for a single polygon.
 * Contains 2D screen-space points and lit color, ready for rendering.
 */
class RenderCommand(
    val commandId: String,
    val points: List<Point2D>,
    val color: IsoColor,
    val originalPath: Path,
    val originalShape: Shape?
)
