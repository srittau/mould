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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

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
        }) {
            D10(actionD10one)
            D10(actionD10two)
            D6(actionD6)
        }
        DiceSection(title = "Progress Rolls", onRoll = {
            progressD10one = (0..9).random()
            progressD10two = (0..9).random()
        }) {
            D10(progressD10one)
            D10(progressD10two)
        }
        DiceSection(title = "Oracle Rolls", onRoll = {
            oracleD100 = (0..9).random()
            oracleD10 = (0..9).random()
        }) {
            D100(oracleD100)
            D10(oracleD10)
        }
    }
}

@Composable
fun DiceSection(title: String, onRoll: () -> Unit = {}, content: @Composable () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.labelLarge)
        DiceGroup(onRoll) {
            content()
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiceViewPreview() {
    DiceView()
}
