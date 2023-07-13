package org.rittau.mould.model

fun rollD100(): Triple<Int, Int, Int> {
    val d100 = (0..9).random()
    val d10 = (0..9).random()
    val total = d100*10 +d10
    return Triple(if (total == 0) 100 else total, d100, d10)
}
