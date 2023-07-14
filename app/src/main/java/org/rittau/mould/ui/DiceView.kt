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
import org.rittau.mould.model.StatOrTrack
import org.rittau.mould.model.rollAction
import org.rittau.mould.model.rollOracle
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
    optionsRow: @Composable () -> Unit,
    resultRow: @Composable () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        DiceGroup(onRoll = onRoll, modifier = Modifier) {
            dice()
        }
        optionsRow()
        resultRow()
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
        mutableStateOf(ActionRoll(10, 10, 6))
    }

    DiceSection(title = "Action Rolls", onRoll = {
        roll = rollAction(character, stat, adds)
    }, {
        D10(roll.challengeRoll1Dice, diceSize = 75.dp, zeroAsTen = true)
        D10(roll.challengeRoll2Dice, diceSize = 75.dp, zeroAsTen = true)
        D6(roll.actionRoll, diceSize = 75.dp, outline = true)
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Score: ${roll.actionRoll} + ${roll.stat} + ${roll.adds} = ${roll.actionScore}")
            Text(roll.result.name)
            if (roll.match) Text("Match!")
        }
    })
}

@Composable
fun ProgressRolls() {
    var progress1 by rememberSaveable {
        mutableStateOf(0)
    }
    var progress2 by rememberSaveable {
        mutableStateOf(0)
    }

    DiceSection(title = "Progress Rolls", onRoll = {
        progress1 = (0..9).random()
        progress2 = (0..9).random()
    }, {
        D10(progress1, diceSize = 75.dp, zeroAsTen = true)
        D10(progress2, diceSize = 75.dp, zeroAsTen = true)
    }, {}, {})
}

@Composable
fun OracleRolls() {
    var oracleValue by rememberSaveable {
        mutableStateOf(100)
    }
    var oracleD100 by rememberSaveable {
        mutableStateOf(0)
    }
    var oracleD10 by rememberSaveable {
        mutableStateOf(0)
    }

    DiceSection(title = "Oracle Rolls", onRoll = {
        val (o, d100, d10) = rollOracle()
        oracleValue = o
        oracleD100 = d100
        oracleD10 = d10
    }, {
        D100(oracleD100, diceSize = 75.dp)
        D10(oracleD10, diceSize = 75.dp)
    }, {}, {
        Text("Oracle roll: $oracleValue")
    })
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiceViewPreview() {
    DiceView(Character(UUID.randomUUID(), "Joe"))
}
