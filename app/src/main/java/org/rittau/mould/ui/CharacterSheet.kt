package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.Character
import org.rittau.mould.ui.theme.MouldTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterSheet(character: Character, onEdit: () -> Unit) {
    val name = character.name.ifBlank { "Unnamed Character" }
    Surface {
        Column {
            TopAppBar(
                title = { Text(text = name) },
                actions = {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            )
            Column(
                verticalArrangement = Arrangement.Top, modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    StatsBlock(character, modifier = Modifier.padding(bottom = 32.dp))
                    ConditionTrack({ HealthIcon(modifier = it) }, character, character.healthTrack)
                    ConditionTrack({ SpiritIcon(modifier = it) }, character, character.spiritTrack)
                    ConditionTrack({ SupplyIcon(modifier = it) }, character, character.supplyTrack)
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterSheetPreview() {
    MouldTheme {
        CharacterSheet(Character("John")) {}
    }
}
