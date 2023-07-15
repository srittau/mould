package org.rittau.mould.model

import java.io.Serializable


data class OracleRoll(
    val rollD100: Int,
    val rollD10: Int,
) : Serializable {
    val value = d100value(rollD100, rollD10)
}

fun rollOracle(): OracleRoll {
    val (d100, d10, _) = rollD100()
    return OracleRoll(d100, d10)
}
