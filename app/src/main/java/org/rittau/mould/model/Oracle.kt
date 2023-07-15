package org.rittau.mould.model

import java.io.Serializable


data class OracleRoll(
    val rollD100: Int,
    val rollD10: Int,
) : Serializable {
    val value = d100value(rollD100, rollD10)
    val match = rollD100 == rollD10
    val almostCertain = value >= 11
    val likely = value >= 26
    val fiftyFifty = value >= 51
    val unlikely = value >= 76
    val smallChance = value >= 91
}

fun rollOracle(): OracleRoll {
    val (d100, d10, _) = rollD100()
    return OracleRoll(d100, d10)
}
