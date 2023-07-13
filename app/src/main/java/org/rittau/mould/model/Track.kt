package org.rittau.mould.model

import java.io.Serializable

const val MAX_TICKS = 40
val TICKS_RANGE = 0..MAX_TICKS


open class Track(
    var name: String,
    ticks: Int = 0,
) : Serializable {
    var ticks: Int = ticks
        set(value) {
            if (!TICKS_RANGE.contains(value)) {
                throw IllegalArgumentException("Progress must be between 0 and $MAX_TICKS")
            }
            field = value
        }

    fun markTick(): Int {
        if (ticks < MAX_TICKS) ticks++
        return ticks
    }
}
