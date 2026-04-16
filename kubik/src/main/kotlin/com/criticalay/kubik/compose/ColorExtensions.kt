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
    r = (red * 255.0).toDouble(),
    g = (green * 255.0).toDouble(),
    b = (blue * 255.0).toDouble(),
    a = (alpha * 255.0).toDouble()
)
