package org.rittau.mould.model

enum class Result {
    StrongHit, WeakHit, Miss
}

fun rollD10(): Int = (0..9).random()

fun rollD100(): Triple<Int, Int, Int> {
    val d100 = rollD10()
    val d10 = rollD10()
    return Triple(d100, d10, d100value(d100, d10))
}

fun d100value(d100: Int, d10: Int): Int {
    val total = d100 * 10 + d10
    return if (total == 0) 100 else total
}
