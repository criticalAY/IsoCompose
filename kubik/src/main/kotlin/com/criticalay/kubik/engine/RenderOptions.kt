package com.criticalay.kubik.engine

/**
 * Configuration options for the rendering pipeline.
 */
class RenderOptions(
    val enableDepthSorting: Boolean = true,
    val enableBackfaceCulling: Boolean = true,
    val enableBoundsChecking: Boolean = true,
    val enableBroadPhaseSort: Boolean = true,
    val broadPhaseCellSize: Double = DEFAULT_BROAD_PHASE_CELL_SIZE
) {
    init {
        require(broadPhaseCellSize.isFinite() && broadPhaseCellSize > 0.0) {
            "broadPhaseCellSize must be positive and finite, got $broadPhaseCellSize"
        }
    }

    fun copy(
        enableDepthSorting: Boolean = this.enableDepthSorting,
        enableBackfaceCulling: Boolean = this.enableBackfaceCulling,
        enableBoundsChecking: Boolean = this.enableBoundsChecking,
        enableBroadPhaseSort: Boolean = this.enableBroadPhaseSort,
        broadPhaseCellSize: Double = this.broadPhaseCellSize
    ): RenderOptions = RenderOptions(
        enableDepthSorting, enableBackfaceCulling, enableBoundsChecking,
        enableBroadPhaseSort, broadPhaseCellSize
    )

    companion object {
        const val DEFAULT_BROAD_PHASE_CELL_SIZE: Double = 100.0

        val Default = RenderOptions()

        val NoDepthSorting = RenderOptions(
            enableDepthSorting = false,
            enableBroadPhaseSort = false
        )

        val NoCulling = RenderOptions(
            enableBackfaceCulling = false,
            enableBoundsChecking = false
        )
    }
}
