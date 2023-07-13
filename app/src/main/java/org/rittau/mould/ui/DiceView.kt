package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
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
import org.rittau.mould.model.rollOracle

@Composable
fun DiceView() {
    var actionD10one by rememberSaveable {
        mutableStateOf(0)
    }
    var actionD10two by rememberSaveable {
        mutableStateOf(0)
    }
    var progressD10one by rememberSaveable {
        mutableStateOf(0)
    }
    var progressD10two by rememberSaveable {
        mutableStateOf(0)
    }
    var actionD6 by rememberSaveable {
        mutableStateOf(6)
    }
    var oracle by rememberSaveable {
        mutableStateOf(100)
    }
    var oracleD100 by rememberSaveable {
        mutableStateOf(0)
    }
    var oracleD10 by rememberSaveable {
        mutableStateOf(0)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        DiceSection(title = "Action Rolls", onRoll = {
            actionD10one = (0..9).random()
            actionD10two = (0..9).random()
            actionD6 = (1..6).random()
        }, {
            D10(actionD10one, diceSize = 75.dp, zeroAsTen = true)
            D10(actionD10two, diceSize = 75.dp, zeroAsTen = true)
            D6(actionD6, diceSize = 75.dp, outline = true)
        }, {})
        DiceSection(title = "Progress Rolls", onRoll = {
            progressD10one = (0..9).random()
            progressD10two = (0..9).random()
        }, {
            D10(progressD10one, diceSize = 75.dp, zeroAsTen = true)
            D10(progressD10two, diceSize = 75.dp, zeroAsTen = true)
        }, {})
        DiceSection(title = "Oracle Rolls", onRoll = {
            val (o, d100, d10) = rollOracle()
            oracle = o
            oracleD100 = d100
            oracleD10 = d10
        }, {
            D100(oracleD100, diceSize = 75.dp)
            D10(oracleD10, diceSize = 75.dp)
        }, {
            Text("Oracle roll: $oracle")
        })
    }
}

@Composable
fun DiceSection(
    title: String,
    onRoll: () -> Unit = {},
    dice: @Composable () -> Unit,
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
        resultRow()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiceViewPreview() {
    DiceView()
}
