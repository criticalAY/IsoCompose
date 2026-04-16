package com.criticalay.kubik.engine

import com.criticalay.kubik.core.IsoColor
import com.criticalay.kubik.core.Point
import com.criticalay.kubik.core.Point2D
import kotlin.math.floor

/**
 * Intersection-based depth sorting with optional broad-phase acceleration.
 * Uses Kahn's topological sort for correct painter's algorithm ordering.
 */
internal object DepthSorter {

    internal data class TransformedItem(
        val item: SceneGraph.SceneItem,
        val transformedPoints: List<Point2D>,
        val litColor: IsoColor
    )

    fun sort(items: List<TransformedItem>, options: RenderOptions): List<TransformedItem> {
        val depthSorted = items.sortedByDescending { it.item.path.depth }
        val sortedItems = mutableListOf<TransformedItem>()
        val observer = Point(-10.0, -10.0, 20.0)
        val length = depthSorted.size

        val drawBefore = List(length) { mutableListOf<Int>() }

        if (options.enableBroadPhaseSort) {
            val candidatePairs = buildBroadPhaseCandidatePairs(depthSorted, options.broadPhaseCellSize)
            for (packed in candidatePairs) {
                val i = (packed ushr 32).toInt()
                val j = (packed and 0xFFFFFFFFL).toInt()
                checkDepthDependency(depthSorted[i], depthSorted[j], i, j, drawBefore, observer)
            }
        } else {
            for (i in 0 until length) {
                for (j in 0 until i) {
                    checkDepthDependency(depthSorted[i], depthSorted[j], i, j, drawBefore, observer)
                }
            }
        }

        // Kahn's algorithm
        val inDegree = IntArray(length)
        var totalEdges = 0
        for (i in 0 until length) {
            inDegree[i] = drawBefore[i].size
            totalEdges += inDegree[i]
        }

        val depCount = IntArray(length)
        for (i in 0 until length) {
            for (j in drawBefore[i]) depCount[j]++
        }
        val depOffsets = IntArray(length + 1)
        for (i in 0 until length) depOffsets[i + 1] = depOffsets[i] + depCount[i]
        val depEdges = IntArray(totalEdges)
        val depFill = IntArray(length)
        for (i in 0 until length) {
            for (j in drawBefore[i]) {
                depEdges[depOffsets[j] + depFill[j]] = i
                depFill[j]++
            }
        }

        val queue = IntArray(length)
        var qHead = 0; var qTail = 0
        for (i in 0 until length) {
            if (inDegree[i] == 0) queue[qTail++] = i
        }

        while (qHead < qTail) {
            val node = queue[qHead++]
            sortedItems.add(depthSorted[node])
            for (k in depOffsets[node] until depOffsets[node + 1]) {
                val dep = depEdges[k]
                if (--inDegree[dep] == 0) queue[qTail++] = dep
            }
        }

        // Fallback for circular dependencies
        for (i in 0 until length) {
            if (inDegree[i] > 0) sortedItems.add(depthSorted[i])
        }

        return sortedItems
    }

    private fun checkDepthDependency(
        itemA: TransformedItem, itemB: TransformedItem,
        i: Int, j: Int,
        drawBefore: List<MutableList<Int>>, observer: Point
    ) {
        if (IntersectionUtils.hasIntersection(
                itemA.transformedPoints.map { Point(it.x, it.y, 0.0) },
                itemB.transformedPoints.map { Point(it.x, it.y, 0.0) }
            )
        ) {
            val cmp = itemA.item.path.closerThan(itemB.item.path, observer)
            if (cmp < 0) drawBefore[i].add(j)
            else if (cmp > 0) drawBefore[j].add(i)
        }
    }

    private fun buildBroadPhaseCandidatePairs(
        items: List<TransformedItem>, cellSize: Double
    ): LongArray {
        val grid = hashMapOf<Long, MutableList<Int>>()

        items.forEachIndexed { index, item ->
            val bounds = item.getBounds()
            val minCol = floor(bounds.minX / cellSize).toInt()
            val maxCol = floor(bounds.maxX / cellSize).toInt()
            val minRow = floor(bounds.minY / cellSize).toInt()
            val maxRow = floor(bounds.maxY / cellSize).toInt()

            for (row in minRow..maxRow) {
                for (col in minCol..maxCol) {
                    grid.getOrPut(cellKey(col, row)) { mutableListOf() }.add(index)
                }
            }
        }

        val seen = hashSetOf<Long>()
        val pairs = mutableListOf<Long>()
        for (bucket in grid.values) {
            if (bucket.size < 2) continue
            for (a in 0 until bucket.lastIndex) {
                for (b in a + 1 until bucket.size) {
                    val first = minOf(bucket[a], bucket[b])
                    val second = maxOf(bucket[a], bucket[b])
                    val key = (first.toLong() shl 32) xor (second.toLong() and 0xffffffffL)
                    if (seen.add(key)) {
                        pairs.add((second.toLong() shl 32) or (first.toLong() and 0xFFFFFFFFL))
                    }
                }
            }
        }
        return pairs.toLongArray()
    }

    private fun cellKey(col: Int, row: Int): Long {
        return (col.toLong() shl 32) xor (row.toLong() and 0xffffffffL)
    }

    private fun TransformedItem.getBounds(): ItemBounds {
        var minX = Double.POSITIVE_INFINITY; var minY = Double.POSITIVE_INFINITY
        var maxX = Double.NEGATIVE_INFINITY; var maxY = Double.NEGATIVE_INFINITY
        for (p in transformedPoints) {
            minX = minOf(minX, p.x); minY = minOf(minY, p.y)
            maxX = maxOf(maxX, p.x); maxY = maxOf(maxY, p.y)
        }
        return ItemBounds(minX, minY, maxX, maxY)
    }

    private data class ItemBounds(val minX: Double, val minY: Double, val maxX: Double, val maxY: Double)
}
