package com.criticalay.kubik.core

/**
 * A polygon face in 3D space, defined by an ordered list of vertices.
 * Requires at least 3 points. Immutable — all transforms return new instances.
 */
open class Path(
    points: List<Point>
) {
    val points: List<Point> = points.toList()

    init {
        require(this.points.size >= 3) { "Path requires at least 3 points, got ${this.points.size}" }
    }

    /** Average depth of all points (default 30-degree angle). */
    val depth: Double = this.points.sumOf { it.depth() } / this.points.size

    fun depth(angle: Double): Double = points.sumOf { it.depth(angle) } / points.size

    constructor(vararg points: Point) : this(points.toList())

    fun reverse(): Path = Path(points.reversed())

    fun translate(dx: Double, dy: Double, dz: Double): Path {
        return Path(points.map { it.translate(dx, dy, dz) })
    }

    fun rotateX(origin: Point, angle: Double): Path = Path(points.map { it.rotateX(origin, angle) })
    fun rotateY(origin: Point, angle: Double): Path = Path(points.map { it.rotateY(origin, angle) })
    fun rotateZ(origin: Point, angle: Double): Path = Path(points.map { it.rotateZ(origin, angle) })

    fun scale(origin: Point, dx: Double, dy: Double, dz: Double): Path {
        return Path(points.map { it.scale(origin, dx, dy, dz) })
    }

    fun scale(origin: Point, dx: Double, dy: Double): Path = Path(points.map { it.scale(origin, dx, dy) })
    fun scale(origin: Point, dx: Double): Path = Path(points.map { it.scale(origin, dx) })

    /**
     * Depth comparison for sorting: positive if this path is closer to observer than [pathA].
     */
    fun closerThan(pathA: Path, observer: Point): Int {
        return pathA.countCloserThan(this, observer) - this.countCloserThan(pathA, observer)
    }

    private fun countCloserThan(pathA: Path, observer: Point): Int {
        val ab = Vector.fromTwoPoints(pathA.points[0], pathA.points[1])
        val ac = Vector.fromTwoPoints(pathA.points[0], pathA.points[2])
        val n = Vector.crossProduct(ab, ac)

        val oa = Vector.fromTwoPoints(Point.ORIGIN, pathA.points[0])
        val ou = Vector.fromTwoPoints(Point.ORIGIN, observer)

        val d = Vector.dotProduct(n, oa)
        val observerPosition = Vector.dotProduct(n, ou) - d

        var result = 0
        var result0 = 0

        for (point in points) {
            val op = Vector.fromTwoPoints(Point.ORIGIN, point)
            val pPosition = Vector.dotProduct(n, op) - d

            if (observerPosition * pPosition >= 0.000000001) {
                result++
            }
            if (observerPosition * pPosition >= -0.000000001 && observerPosition * pPosition < 0.000000001) {
                result0++
            }
        }

        return if (result == 0) 0 else (result + result0) / points.size
    }
}
