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

import androidx.compose.ui.graphics.Path
import com.criticalay.kubik.engine.RenderCommand

/** Convert a [RenderCommand]'s 2D points to a Compose [Path]. */
fun RenderCommand.toComposePath(): Path {
    val path = Path()
    if (points.isEmpty()) return path

    path.moveTo(points[0].x.toFloat(), points[0].y.toFloat())
    for (i in 1 until points.size) {
        path.lineTo(points[i].x.toFloat(), points[i].y.toFloat())
    }
    path.close()
    return path
}
