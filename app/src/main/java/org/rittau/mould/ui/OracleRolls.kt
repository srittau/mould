package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
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
    }, {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
                OddsRow("Almost Certain", roll.almostCertain)
                OddsRow("Likely", roll.likely)
                OddsRow("50/50", roll.fiftyFifty)
                OddsRow("Unlikely", roll.unlikely)
                OddsRow("Small Chance", roll.smallChance)
            }
            if (roll.match) RollMatch(modifier = Modifier.rotate(90f))
        }
    }, {}, {
        Text("Oracle roll: ${roll.value}")
    })
}

@Composable
fun OddsRow(text: String, matched: Boolean) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        val icon = if (matched) Icons.Filled.Check else Icons.Filled.Close
        val description = if (matched) "Yes" else "No"
        val color = if (matched) 0xFF00CC00 else 0xFFCC0000
        Icon(icon, description, tint = Color(color), modifier = Modifier.size(16.dp))
        Text(text, style = MaterialTheme.typography.bodySmall)
    }
}
