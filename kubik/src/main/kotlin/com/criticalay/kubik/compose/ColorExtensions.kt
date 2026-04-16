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
import com.criticalay.kubik.core.IsoColor

/** Convert [IsoColor] (0-255) to Compose [Color] (0-1). */
fun IsoColor.toComposeColor(): Color = Color(
    red = (r / 255.0).toFloat(),
    green = (g / 255.0).toFloat(),
    blue = (b / 255.0).toFloat(),
    alpha = (a / 255.0).toFloat()
)

/** Convert Compose [Color] to [IsoColor]. */
fun Color.toIsoColor(): IsoColor = IsoColor(
    r = (red * 255.0),
    g = (green * 255.0),
    b = (blue * 255.0),
    a = (alpha * 255.0)
)
