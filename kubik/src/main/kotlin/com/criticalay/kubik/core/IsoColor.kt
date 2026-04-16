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

package com.criticalay.kubik.core

import kotlin.math.max
import kotlin.math.min
import kotlin.math.round

/**
 * Color with RGB (0-255) and lazy HSL representation.
 * Used for lighting calculations in the isometric renderer.
 */
data class IsoColor(
    val r: Double,
    val g: Double,
    val b: Double,
    val a: Double = 255.0
) {
    constructor(r: Int, g: Int, b: Int, a: Int = 255) : this(
        r.toDouble(), g.toDouble(), b.toDouble(), a.toDouble()
    )

    private val hsl: Triple<Double, Double, Double> by lazy(LazyThreadSafetyMode.NONE) { computeHsl() }

    val h: Double get() = hsl.first
    val s: Double get() = hsl.second
    val l: Double get() = hsl.third

    init {
        require(r in 0.0..255.0) { "r must be in 0..255, got $r" }
        require(g in 0.0..255.0) { "g must be in 0..255, got $g" }
        require(b in 0.0..255.0) { "b must be in 0..255, got $b" }
        require(a in 0.0..255.0) { "a must be in 0..255, got $a" }
    }

    private fun computeHsl(): Triple<Double, Double, Double> {
        val rN = r / 255.0
        val gN = g / 255.0
        val bN = b / 255.0

        val cMax = max(rN, max(gN, bN))
        val cMin = min(rN, min(gN, bN))

        var hue = 0.0
        val lightness = (cMax + cMin) / 2.0
        val saturation: Double

        if (cMax == cMin) {
            saturation = 0.0
        } else {
            val d = cMax - cMin
            saturation = if (lightness > 0.5) d / (2.0 - cMax - cMin) else d / (cMax + cMin)
            when (cMax) {
                rN -> hue = (gN - bN) / d + (if (gN < bN) 6.0 else 0.0)
                gN -> hue = (bN - rN) / d + 2.0
                bN -> hue = (rN - gN) / d + 4.0
            }
            hue /= 6.0
        }

        return Triple(hue, saturation, lightness)
    }

    /**
     * Blend with [lightColor] and adjust lightness for directional lighting.
     */
    fun lighten(percentage: Double, lightColor: IsoColor): IsoColor {
        val blended = IsoColor(
            (lightColor.r / 255.0) * r,
            (lightColor.g / 255.0) * g,
            (lightColor.b / 255.0) * b,
            a
        )
        val newLightness = min(blended.l + percentage, 1.0)
        return blended.withLightness(newLightness)
    }

    private fun withLightness(newLightness: Double): IsoColor {
        val (rNew, gNew, bNew) = hslToRgb(h, s, newLightness)
        return IsoColor(
            rNew.coerceIn(0.0, 255.0),
            gNew.coerceIn(0.0, 255.0),
            bNew.coerceIn(0.0, 255.0),
            a
        )
    }

    private fun hslToRgb(h: Double, s: Double, l: Double): Triple<Double, Double, Double> {
        if (s == 0.0) return Triple(l * 255.0, l * 255.0, l * 255.0)

        val q = if (l < 0.5) l * (1 + s) else l + s - l * s
        val p = 2.0 * l - q
        return Triple(
            hue2rgb(p, q, h + 1.0 / 3.0) * 255.0,
            hue2rgb(p, q, h) * 255.0,
            hue2rgb(p, q, h - 1.0 / 3.0) * 255.0
        )
    }

    private fun hue2rgb(p: Double, q: Double, t: Double): Double {
        var tN = t
        if (tN < 0) tN += 1
        if (tN > 1) tN -= 1
        if (tN < 1.0 / 6.0) return p + (q - p) * 6.0 * tN
        if (tN < 1.0 / 2.0) return q
        if (tN < 2.0 / 3.0) return p + (q - p) * (2.0 / 3.0 - tN) * 6.0
        return p
    }

    fun toRGBA(): IntArray = intArrayOf(
        round(r).toInt(), round(g).toInt(), round(b).toInt(), round(a).toInt()
    )

    companion object {
        val WHITE = IsoColor(255, 255, 255)
        val BLACK = IsoColor(0, 0, 0)
        val RED = IsoColor(255, 0, 0)
        val GREEN = IsoColor(0, 255, 0)
        val BLUE = IsoColor(0, 0, 255)
        val GRAY = IsoColor(158, 158, 158)
        val DARK_GRAY = IsoColor(97, 97, 97)
        val LIGHT_GRAY = IsoColor(224, 224, 224)
        val CYAN = IsoColor(0, 188, 212)
        val ORANGE = IsoColor(255, 152, 0)
        val PURPLE = IsoColor(156, 39, 176)
        val YELLOW = IsoColor(255, 235, 59)
        val BROWN = IsoColor(121, 85, 72)

        fun fromHex(hex: Long): IsoColor = when {
            hex < 0 -> fromPackedArgbInt(hex.toInt())
            hex <= 0xFFFFFFL -> fromPackedRgbInt(hex.toInt())
            hex <= 0xFFFFFFFFL -> fromPackedArgbInt(hex.toInt())
            else -> throw IllegalArgumentException("Hex color must fit in 32 bits, got $hex")
        }

        fun fromHex(hex: String): IsoColor {
            val normalized = hex.removePrefix("#").removePrefix("0x").removePrefix("0X")
            require(normalized.length == 6 || normalized.length == 8) {
                "Hex color must be 6 (RRGGBB) or 8 (AARRGGBB) digits, got '$hex'"
            }
            val value = normalized.toLong(16)
            return if (normalized.length == 6) fromPackedRgbInt(value.toInt())
            else fromPackedArgbInt(value.toInt())
        }

        fun fromArgb(a: Int, r: Int, g: Int, b: Int): IsoColor = IsoColor(r, g, b, a)

        private fun fromPackedRgbInt(rgb: Int): IsoColor = IsoColor(
            (rgb ushr 16) and 0xFF, (rgb ushr 8) and 0xFF, rgb and 0xFF
        )

        private fun fromPackedArgbInt(argb: Int): IsoColor = IsoColor(
            (argb ushr 16) and 0xFF, (argb ushr 8) and 0xFF, argb and 0xFF, (argb ushr 24) and 0xFF
        )
    }
}
