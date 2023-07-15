package org.rittau.mould.model

import java.io.Serializable

data class ProgressRoll(val roll1: Int, val roll2: Int) : Serializable {
}

fun rollProgress(): ProgressRoll {
    val roll1 = rollD10()
    val roll2 = rollD10()
    return ProgressRoll(roll1, roll2)
}
