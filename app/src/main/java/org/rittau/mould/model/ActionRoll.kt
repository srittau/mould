package org.rittau.mould.model

import java.io.Serializable
import kotlin.math.min


data class ActionRoll(
    val challengeRoll1: Int,
    val challengeRoll2: Int,
    val actionRoll: Int,
    val stat: Int = 0,
    val adds: Int = 0,
) : Serializable {
    private val challenge1 = if (challengeRoll1 == 0) 10 else challengeRoll1
    private val challenge2 = if (challengeRoll2 == 0) 10 else challengeRoll2
    val actionScore = min(actionRoll + stat + adds, 10)
    val match = challengeRoll1 == challengeRoll2
    val result = when {
        (actionScore > challenge1 && actionScore > challenge2) -> Result.StrongHit
        (actionScore > challenge1 || actionScore > challenge2) -> Result.WeakHit
        else -> Result.Miss
    }
}

fun rollAction(character: Character, stat: StatOrTrack?, adds: Int = 0): ActionRoll {
    val statValue = if (stat != null) character.statOrTrackValue(stat) else 0
    val challengeRoll1 = rollD10()
    val challengeRoll2 = rollD10()
    val actionRoll = (1..6).random()
    return ActionRoll(challengeRoll1, challengeRoll2, actionRoll, statValue, adds)
}
