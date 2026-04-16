package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A torus (donut shape) approximated by quad faces.
 *
 * The torus lies in the xy-plane centered at [position], with the tube
 * centered at distance [majorRadius] from center and tube radius [minorRadius].
 *
 * @param position Center of the torus
 * @param majorRadius Distance from center to tube center (must be positive, default 1.5)
 * @param minorRadius Radius of the tube cross-section (must be positive, default 0.5)
 * @param majorSegments Number of segments around the ring (minimum 3, default 20)
 * @param minorSegments Number of segments around the tube (minimum 3, default 12)
 */
class Torus(
    val position: Point = Point.ORIGIN,
    val majorRadius: Double = 1.5,
    val minorRadius: Double = 0.5,
    val majorSegments: Int = 20,
    val minorSegments: Int = 12
) : Shape(createPaths(position, majorRadius, minorRadius, majorSegments, minorSegments)) {
    init {
        require(majorRadius > 0.0) { "Torus majorRadius must be positive" }
        require(minorRadius > 0.0) { "Torus minorRadius must be positive" }
        require(majorSegments >= 3) { "Torus needs at least 3 major segments" }
        require(minorSegments >= 3) { "Torus needs at least 3 minor segments" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Torus =
        Torus(position.translate(dx, dy, dz), majorRadius, minorRadius, majorSegments, minorSegments)

    companion object {
        private fun createPaths(
            position: Point,
            majorRadius: Double, minorRadius: Double,
            majorSegments: Int, minorSegments: Int
        ): List<Path> {
            val paths = mutableListOf<Path>()

            // Generate vertex grid: vertices[major][minor]
            // For each position around the ring (major angle theta),
            // create a circle of points (minor angle phi) in the tube cross-section.
            val vertices = Array(majorSegments) { i ->
                val theta = 2.0 * PI * i / majorSegments
                val cx = position.x + majorRadius * cos(theta)
                val cy = position.y + majorRadius * sin(theta)

                Array(minorSegments) { j ->
                    val phi = 2.0 * PI * j / minorSegments
                    // The tube circle is in the plane defined by the radial direction and z-axis
                    val tubeX = minorRadius * cos(phi) * cos(theta)
                    val tubeY = minorRadius * cos(phi) * sin(theta)
                    val tubeZ = minorRadius * sin(phi)

                    Point(cx + tubeX, cy + tubeY, position.z + tubeZ)
                }
            }

            // Connect adjacent cross-sections with quads
            for (i in 0 until majorSegments) {
                val nextI = (i + 1) % majorSegments
                for (j in 0 until minorSegments) {
                    val nextJ = (j + 1) % minorSegments
                    paths.add(
                        Path(
                            vertices[i][j],
                            vertices[nextI][j],
                            vertices[nextI][nextJ],
                            vertices[i][nextJ]
                        )
                    )
                }
            }

            return paths
        }
    }
}
