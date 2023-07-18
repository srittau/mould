package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import org.rittau.mould.model.Character
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.StatOrTrack
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSheet(model: MouldModel, navigation: MouldNavigation) {
    val name = model.character.name.ifBlank { "Unnamed Character" }
    Surface {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            TopAppBar(
                title = { Text(text = name) },
                actions = {
                    IconButton(onClick = { navigation.onEditCharacter() }) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(16.dp),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatsBlock(
                        model.character,
                        modifier = Modifier.padding(bottom = 32.dp),
                        onClick = { navigation.onStatOrTrackClicked(it) },
                    )
                    MomentumTrack(model.character)
                    ConditionTrack(
                        { HealthIcon(modifier = it) },
                        model.character,
                        model.character.healthTrack,
                        onClick = { navigation.onStatOrTrackClicked(StatOrTrack.Health) },
                    )
                    ConditionTrack(
                        { SpiritIcon(modifier = it) },
                        model.character,
                        model.character.spiritTrack,
                        onClick = { navigation.onStatOrTrackClicked(StatOrTrack.Spirit) },
                    )
                    ConditionTrack(
                        { SupplyIcon(modifier = it) },
                        model.character,
                        model.character.supplyTrack,
                        onClick = { navigation.onStatOrTrackClicked(StatOrTrack.Supply) },
                    )
                }
                Divider()
                CharacterNotes(model.character)
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterSheetPreview() {
    val model = MouldModel()
    model.setCharacter(Character(UUID.randomUUID(), "John"), emptyList(), emptyList(), emptyList())
    val navigation = MouldNavigation(NavHostController(LocalContext.current))
    MouldTheme {
        CharacterSheet(model, navigation)
    }
}
