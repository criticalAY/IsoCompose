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
