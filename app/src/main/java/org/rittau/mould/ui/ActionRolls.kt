package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.ActionRoll
import org.rittau.mould.model.Character
import org.rittau.mould.model.StatOrTrack
import org.rittau.mould.model.rollAction
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

@Composable
fun ActionRolls(character: Character, stat: StatOrTrack?, onChangeStat: (StatOrTrack?) -> Unit) {
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
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            val add = if (stat != null) character.statOrTrackValue(stat) else 0
            LabeledOption("Stat or Track", add) {
                StatSelect(character, stat, modifier = Modifier.fillMaxWidth()) { onChangeStat(it) }
            }
            LabeledOption("Adds", adds) {
                Slider(
                    value = adds.toFloat(),
                    onValueChange = { adds = it.toInt() },
                    valueRange = 0f..5f,
                    steps = 4,
                )
            }
        }
        LabeledOption("Action Score") {
            Text("${roll.actionRoll} + ${roll.stat} + ${roll.adds} = ${roll.actionScore}")
        }
    })
}

@Composable
fun LabeledOption(label: String, adds: Int? = null, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall)
            if (adds != null) {
                Text(
                    "+${adds}",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        content()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ActionRollsPreview() {
    MouldTheme {
        ActionRolls(Character(UUID.randomUUID(), "Joe"), null) {}
    }
}
