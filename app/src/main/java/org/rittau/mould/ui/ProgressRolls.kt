package org.rittau.mould.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ProgressRoll
import org.rittau.mould.model.rollProgress

@Composable
fun ProgressRolls() {
    var roll by rememberSaveable {
        mutableStateOf(ProgressRoll(10, 10))
    }

    DiceSection(title = "Progress Rolls", onRoll = {
        roll = rollProgress()
    }, {
        D10(roll.roll1, diceSize = 75.dp, zeroIsTen = true)
        D10(roll.roll2, diceSize = 75.dp, zeroIsTen = true)
    }, {}, {})
}

