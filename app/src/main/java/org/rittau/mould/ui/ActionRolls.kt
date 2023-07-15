package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ActionRoll
import org.rittau.mould.model.Character
import org.rittau.mould.model.StatOrTrack
import org.rittau.mould.model.rollAction

@Composable
fun ActionRolls(character: Character) {
    var stat by rememberSaveable {
        mutableStateOf<StatOrTrack?>(null)
    }
    var adds by rememberSaveable {
        mutableStateOf(0)
    }
    var roll by rememberSaveable {
        mutableStateOf(ActionRoll(0, 0, 6))
    }

    DiceSection(title = "Action Rolls", onRoll = {
        roll = rollAction(character, stat, adds)
    }, {
        D10(roll.challengeRoll1, diceSize = 75.dp, zeroIsTen = true)
        D10(roll.challengeRoll2, diceSize = 75.dp, zeroIsTen = true)
        D6(roll.actionRoll, diceSize = 75.dp, outline = true)
    }, {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 10.dp),
        ) {
            RollResult(roll.result)
            if (roll.match) RollMatch()
        }
    }, {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            StatSelect(character, stat) { stat = it }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Slider(
                    value = adds.toFloat(),
                    onValueChange = { adds = it.toInt() },
                    valueRange = 0f..5f,
                    steps = 4,
                    modifier = Modifier.width(300.dp)
                )
                Text("+${adds}")
            }
        }
    }, {
        Text("Action score: ${roll.actionRoll} + ${roll.stat} + ${roll.adds} = ${roll.actionScore}")
    })
}
