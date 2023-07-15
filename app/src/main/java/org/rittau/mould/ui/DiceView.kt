package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ActionRoll
import org.rittau.mould.model.Character
import org.rittau.mould.model.OracleRoll
import org.rittau.mould.model.ProgressRoll
import org.rittau.mould.model.StatOrTrack
import org.rittau.mould.model.rollAction
import org.rittau.mould.model.rollOracle
import org.rittau.mould.model.rollProgress
import java.util.UUID

@Composable
fun DiceView(character: Character) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        ActionRolls(character)
        Divider()
        ProgressRolls()
        Divider()
        OracleRolls()
    }
}

@Composable
fun DiceSection(
    title: String,
    onRoll: () -> Unit = {},
    dice: @Composable () -> Unit,
    result: @Composable () -> Unit = {},
    options: @Composable () -> Unit = {},
    calculation: @Composable () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            DiceGroup(onRoll = onRoll, modifier = Modifier) {
                dice()
            }
            result()
        }
        options()
        calculation()
    }
}

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
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
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

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiceViewPreview() {
    DiceView(Character(UUID.randomUUID(), "Joe"))
}
