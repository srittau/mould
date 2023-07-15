package org.rittau.mould.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.OracleRoll
import org.rittau.mould.model.rollOracle

@Composable
fun OracleRolls() {
    var roll by rememberSaveable {
        mutableStateOf(OracleRoll(0, 0))
    }

    DiceSection(title = "Oracle Rolls", onRoll = {
        roll = rollOracle()
    }, {
        D100(roll.rollD100, diceSize = 75.dp)
        D10(roll.rollD10, diceSize = 75.dp)
    }, {}, {
        Text("Oracle roll: ${roll.value}")
    })
}
