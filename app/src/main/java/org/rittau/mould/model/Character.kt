package org.rittau.mould.model

import java.io.Serializable

const val MIN_MOMENTUM = -6
const val MAX_MOMENTUM = 10

const val MIN_STATUS = 0
const val MAX_STATUS = 5

class Character(
    var name: String,
    var summary: String = "",
    var experience: Int = 0,
    var spentExperience: Int = 0,
    var bonds: Int = 0,
    var edge: Int = 1,
    var heart: Int = 1,
    var iron: Int = 1,
    var shadow: Int = 1,
    var wits: Int = 1,
    var momentum: Int = 2,
    var health: Int = 5,
    var spirit: Int = 5,
    var supply: Int = 5,
    var wounded: Boolean = false,
    var unprepared: Boolean = false,
    var shaken: Boolean = false,
    var encumbered: Boolean = false,
    var maimed: Boolean = false,
    var corrupted: Boolean = false,
    var cursed: Boolean = false,
    var tormented: Boolean = false,
) : Serializable {
    //
    // Experience & Bonds
    //

    fun gainExperience(amount: Int): Int {
        experience += amount; return experience
    }

    fun spendExperience(amount: Int): Int {
        if (amount > experience - spentExperience) {
            throw IllegalArgumentException("Not enough experience")
        }
        spentExperience += amount
        return spentExperience
    }

    fun gainBond(): Int {
        bonds++; return bonds
    }

    //
    // Momentum
    //

    val maxMomentum: Int
        get() = MAX_MOMENTUM - debilityCount
    val momentumReset: Int
        get() = when (debilityCount) {
            0 -> 2
            1 -> 1
            else -> 0
        }
    val canGainMomentum: Boolean
        get() = momentum < maxMomentum
    val canLoseMomentum: Boolean
        get() = momentum > MIN_MOMENTUM

    fun gainMomentum(): Int {
        if (canGainMomentum) momentum++; return momentum
    }

    fun loseMomentum(): Int {
        if (canLoseMomentum) momentum--; return momentum
    }

    fun resetMomentum(): Int {
        momentum = momentumReset; return momentum
    }

    //
    // Condition Tracks
    //

    val canGainHealth: Boolean
        get() = !wounded && health < MAX_STATUS
    val canLoseHealth: Boolean
        get() = health > MIN_STATUS

    fun gainHealth(): Int {
        if (canGainHealth) health++; return health
    }

    fun loseHealth(): Int {
        if (canLoseHealth) health--; return health
    }

    val healthTrack: ConditionTrack
        get() = HealthTrack(this)

    val canGainSpirit: Boolean
        get() = !shaken && spirit < MAX_STATUS
    val canLoseSpirit: Boolean
        get() = spirit > MIN_STATUS

    fun gainSpirit(): Int {
        if (canGainSpirit) spirit++; return spirit
    }

    fun loseSpirit(): Int {
        if (canLoseSpirit) spirit--; return spirit
    }

    val spiritTrack: ConditionTrack
        get() = SpiritTrack(this)

    val canGainSupply: Boolean
        get() = !unprepared && supply < MAX_STATUS
    val canLoseSupply: Boolean
        get() = supply > MIN_STATUS

    fun gainSupply(): Int {
        if (canGainSupply) supply++; return supply
    }

    fun loseSupply(): Int {
        if (canLoseSupply) supply--; return supply
    }

    val supplyTrack: ConditionTrack
        get() = SupplyTrack(this)

    //
    // Debilities
    //

    private val debilityCount: Int
        get() {
            var count = 0
            if (wounded) count++
            if (unprepared) count++
            if (shaken) count++
            if (encumbered) count++
            if (maimed) count++
            if (corrupted) count++
            if (cursed) count++
            if (tormented) count++
            return count
        }
}

interface ConditionTrack {
    val canGain: Boolean
    val canLose: Boolean

    val value: Int

    fun gain(): Int {
        return 0
    }

    fun lose(): Int {
        return 0
    }
}

class HealthTrack(val character: Character) : ConditionTrack {
    override val canGain: Boolean
        get() = character.canGainHealth
    override val canLose: Boolean
        get() = character.canLoseHealth

    override val value: Int
        get() = character.health

    override fun gain(): Int {
        return character.gainHealth()
    }

    override fun lose(): Int {
        return character.loseHealth()
    }
}

class SpiritTrack(val character: Character) : ConditionTrack {
    override val canGain: Boolean
        get() = character.canGainSpirit
    override val canLose: Boolean
        get() = character.canLoseSpirit

    override val value: Int
        get() = character.spirit

    override fun gain(): Int {
        return character.gainSpirit()
    }

    override fun lose(): Int {
        return character.loseSpirit()
    }
}

class SupplyTrack(val character: Character) : ConditionTrack {
    override val canGain: Boolean
        get() = character.canGainSupply
    override val canLose: Boolean
        get() = character.canLoseSupply

    override val value: Int
        get() = character.supply

    override fun gain(): Int {
        return character.gainSupply()
    }

    override fun lose(): Int {
        return character.loseSupply()
    }
}

fun statsOk(edge: Int, heart: Int, iron: Int, shadow: Int, wits: Int): Boolean {
    val count3 = listOf(edge, heart, iron, shadow, wits).count { it == 3 }
    val count2 = listOf(edge, heart, iron, shadow, wits).count { it == 2 }
    val count1 = listOf(edge, heart, iron, shadow, wits).count { it == 1 }
    return count3 == 1 && count2 == 2 && count1 == 2
}
