package org.rittau.mould.model

import java.io.Serializable
import kotlin.math.min

enum class Result {
    StrongHit, WeakHit, Miss
}

fun rollD100(): Triple<Int, Int, Int> {
    val d100 = (0..9).random()
    val d10 = (0..9).random()
    val total = d100 * 10 + d10
    return Triple(if (total == 0) 100 else total, d100, d10)
}

data class ActionRoll(
    val challengeRoll1: Int,
    val challengeRoll2: Int,
    val actionRoll: Int,
    val stat: Int = 0,
    val adds: Int = 0,
) : Serializable {
    val challengeRoll1Dice = if (challengeRoll1 == 10) 0 else challengeRoll1
    val challengeRoll2Dice = if (challengeRoll2 == 10) 0 else challengeRoll2
    val actionScore = min(actionRoll + stat + adds, 10)
    val match = challengeRoll1 == challengeRoll2
    val result = when {
        (actionScore > challengeRoll1 && actionScore > challengeRoll2) -> Result.StrongHit
        (actionScore > challengeRoll1 || actionScore > challengeRoll2) -> Result.WeakHit
        else -> Result.Miss
    }
}

fun rollAction(character: Character, stat: StatOrTrack?, adds: Int = 0): ActionRoll {
    val statValue = if (stat != null) character.statOrTrackValue(stat) else 0
    val challengeRoll1 = (1..10).random()
    val challengeRoll2 = (1..10).random()
    val actionRoll = (1..6).random()
    return ActionRoll(challengeRoll1, challengeRoll2, actionRoll, statValue, adds)
}
