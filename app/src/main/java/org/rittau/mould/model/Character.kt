package org.rittau.mould.model

import java.io.Serializable

data class Character(
    var name: String,
    var summary: String = "",
    var edge: Int = 1,
    var heart: Int = 1,
    var iron: Int = 1,
    var shadow: Int = 1,
    var wits: Int = 1,
) : Serializable

fun statsOk(edge: Int, heart: Int, iron: Int, shadow: Int, wits: Int): Boolean {
    val count3 = listOf(edge, heart, iron, shadow, wits).count { it == 3 }
    val count2 = listOf(edge, heart, iron, shadow, wits).count { it == 2 }
    val count1 = listOf(edge, heart, iron, shadow, wits).count { it == 1 }
    return count3 == 1 && count2 == 2 && count1 == 2
}
