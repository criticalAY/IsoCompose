package com.criticalay.kubik.engine

/**
 * Search order for hit testing.
 */
enum class HitOrder {
    /** Return the frontmost (closest to viewer) match. */
    FRONT_TO_BACK,
    /** Return the rearmost (furthest from viewer) match. */
    BACK_TO_FRONT
}
