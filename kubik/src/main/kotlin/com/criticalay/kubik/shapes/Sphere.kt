package com.criticalay.kubik.shapes

import com.criticalay.kubik.core.Path
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Shape
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * A sphere approximated by latitude rings connected by quad/triangle faces.
 *
 * @param position Center of the sphere
 * @param radius Radius (must be positive, default 1.0)
 * @param latSegments Number of latitude rings (minimum 4, default 12)
 * @param lonSegments Number of longitude segments per ring (minimum 3, default 16)
 */
class Sphere(
    val position: Point = Point.ORIGIN,
    val radius: Double = 1.0,
    val latSegments: Int = 12,
    val lonSegments: Int = 16
) : Shape(createPaths(position, radius, latSegments, lonSegments)) {
    init {
        require(radius > 0.0) { "Sphere radius must be positive" }
        require(latSegments >= 4) { "Sphere needs at least 4 latitude segments" }
        require(lonSegments >= 3) { "Sphere needs at least 3 longitude segments" }
    }

    override fun translate(dx: Double, dy: Double, dz: Double): Sphere =
        Sphere(position.translate(dx, dy, dz), radius, latSegments, lonSegments)

    companion object {
        private fun createPaths(
            position: Point, radius: Double, latSegments: Int, lonSegments: Int
        ): List<Path> {
            val paths = mutableListOf<Path>()

            // Generate vertex grid: rings[lat][lon]
            // lat=0 is south pole, lat=latSegments is north pole
            val rings = Array(latSegments + 1) { lat ->
                val phi = PI * lat / latSegments // 0 (south) to PI (north)
                val z = position.z + radius * cos(phi)
                val ringRadius = radius * sin(phi)

                Array(lonSegments) { lon ->
                    val theta = 2.0 * PI * lon / lonSegments
                    Point(
                        position.x + ringRadius * cos(theta),
                        position.y + ringRadius * sin(theta),
                        z
                    )
                }
            }

            // South pole cap: triangles from pole to first ring
            val southPole = Point(position.x, position.y, position.z + radius * cos(0.0)) // z + radius (cos(0)=1? No, cos(0)=1 but at lat=0 phi=0, cos(0)=1 means south is +z)
            // Actually: lat=0 → phi=0 → cos(0)=1 → z = pos.z + radius → that's the top
            // Let me fix: lat=0 is the TOP (north pole), lat=latSegments is the BOTTOM (south pole)
            // No wait, phi goes 0→PI. cos(0)=1 so z=pos.z+radius (top). cos(PI)=-1 so z=pos.z-radius (bottom). That's correct:
            // lat=0 → north pole (top), lat=latSegments → south pole (bottom)

            // North pole cap: triangles from top ring to north pole
            for (lon in 0 until lonSegments) {
                val nextLon = (lon + 1) % lonSegments
                paths.add(
                    Path(
                        rings[0][lon],    // north pole (all same point, ringRadius ≈ 0)
                        rings[1][lon],
                        rings[1][nextLon]
                    )
                )
            }

            // Middle bands: quads between adjacent rings
            for (lat in 1 until latSegments - 1) {
                for (lon in 0 until lonSegments) {
                    val nextLon = (lon + 1) % lonSegments
                    paths.add(
                        Path(
                            rings[lat][lon],
                            rings[lat + 1][lon],
                            rings[lat + 1][nextLon],
                            rings[lat][nextLon]
                        )
                    )
                }
            }

            // South pole cap: triangles from bottom ring to south pole
            val lastRing = latSegments - 1
            for (lon in 0 until lonSegments) {
                val nextLon = (lon + 1) % lonSegments
                paths.add(
                    Path(
                        rings[lastRing][lon],
                        rings[latSegments][lon], // south pole (all same point, ringRadius ≈ 0)
                        rings[lastRing][nextLon]
                    )
                )
            }

            return paths
        }
    }
}
