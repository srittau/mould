package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rittau.mould.model.Character
import org.rittau.mould.model.MouldModel
import java.util.UUID

class DiceScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen {
    override val screen = MouldScreenType.Dice

    @Composable
    override fun Content() {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            ActionRolls(model.character, navigation)
            Divider()
            ProgressRolls()
            Divider()
            OracleRolls()
        }
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
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            DiceGroup(onRoll = onRoll, modifier = Modifier) {
                dice()
            }
            result()
        }
        options()
        calculation()
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DiceScreenPreview() {
    val model = MouldModel()
    model.setCharacter(Character(UUID.randomUUID(), "Joe"), emptyList(), emptyList(), emptyList())
    val nav = MouldNavigation(NavHostController(LocalContext.current))
    DiceScreen(model, nav).Content()
}
