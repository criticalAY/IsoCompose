package com.criticalay.kubik.engine

import com.criticalay.kubik.core.Point

/**
 * Geometric intersection and point-in-polygon tests.
 */
internal object IntersectionUtils {

    /** Test if point (x,y) is within radius of polygon edges. */
    fun isPointCloseToPoly(poly: List<Point>, x: Double, y: Double, radius: Double): Boolean {
        val p = Point(x, y, 0.0)
        for (i in poly.indices) {
            val j = (i + 1) % poly.size
            if (Point.distanceToSegment(p, poly[i], poly[j]) < radius) {
                return true
            }
        }
        return false
    }

    /** Ray-casting point-in-polygon test. */
    fun isPointInPoly(poly: List<Point>, x: Double, y: Double): Boolean {
        var c = false
        var j = poly.size - 1
        for (i in poly.indices) {
            if (((poly[i].y <= y && y < poly[j].y) || (poly[j].y <= y && y < poly[i].y)) &&
                (x < (poly[j].x - poly[i].x) * (y - poly[i].y) / (poly[j].y - poly[i].y) + poly[i].x)
            ) {
                c = !c
            }
            j = i
        }
        return c
    }

    /** Test polygon overlap using SAT with bounding-box quick rejection. */
    fun hasIntersection(pointsA: List<Point>, pointsB: List<Point>): Boolean {
        if (pointsA.isEmpty() || pointsB.isEmpty()) return false

        // Bounding box check
        var aMinX = pointsA[0].x; var aMinY = pointsA[0].y
        var aMaxX = aMinX; var aMaxY = aMinY
        var bMinX = pointsB[0].x; var bMinY = pointsB[0].y
        var bMaxX = bMinX; var bMaxY = bMinY

        for (p in pointsA) {
            aMinX = min(aMinX, p.x); aMinY = min(aMinY, p.y)
            aMaxX = max(aMaxX, p.x); aMaxY = max(aMaxY, p.y)
        }
        for (p in pointsB) {
            bMinX = min(bMinX, p.x); bMinY = min(bMinY, p.y)
            bMaxX = max(bMaxX, p.x); bMaxY = max(bMaxY, p.y)
        }

        if (!(((aMinX <= bMinX && bMinX <= aMaxX) || (bMinX <= aMinX && aMinX <= bMaxX)) &&
                    ((aMinY <= bMinY && bMinY <= aMaxY) || (bMinY <= aMinY && aMinY <= bMaxY)))
        ) return false

        // Edge crossing test
        val polyA = pointsA + pointsA[0]
        val polyB = pointsB + pointsB[0]
        val lenA = polyA.size - 1
        val lenB = polyB.size - 1

        val daX = DoubleArray(lenA); val daY = DoubleArray(lenA); val rA = DoubleArray(lenA)
        for (i in 0 until lenA) {
            daX[i] = polyA[i + 1].x - polyA[i].x
            daY[i] = polyA[i + 1].y - polyA[i].y
            rA[i] = daX[i] * polyA[i].y - daY[i] * polyA[i].x
        }

        val dbX = DoubleArray(lenB); val dbY = DoubleArray(lenB); val rB = DoubleArray(lenB)
        for (i in 0 until lenB) {
            dbX[i] = polyB[i + 1].x - polyB[i].x
            dbY[i] = polyB[i + 1].y - polyB[i].y
            rB[i] = dbX[i] * polyB[i].y - dbY[i] * polyB[i].x
        }

        for (i in 0 until lenA) {
            for (j in 0 until lenB) {
                if (daX[i] * dbY[j] != daY[i] * dbX[j]) {
                    val s1a = daY[i] * polyB[j].x - daX[i] * polyB[j].y + rA[i]
                    val s1b = daY[i] * polyB[j + 1].x - daX[i] * polyB[j + 1].y + rA[i]
                    val s2a = dbY[j] * polyA[i].x - dbX[j] * polyA[i].y + rB[j]
                    val s2b = dbY[j] * polyA[i + 1].x - dbX[j] * polyA[i + 1].y + rB[j]
                    if (s1a * s1b < -0.000000001 && s2a * s2b < -0.000000001) return true
                }
            }
        }

        // Containment test
        for (i in 0 until lenA) {
            if (isPointInPoly(pointsB, polyA[i].x, polyA[i].y)) return true
        }
        for (i in 0 until lenB) {
            if (isPointInPoly(pointsA, polyB[i].x, polyB[i].y)) return true
        }

        return false
    }

    private fun min(a: Double, b: Double) = if (a < b) a else b
    private fun max(a: Double, b: Double) = if (a > b) a else b
}
