package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.sp
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
                    Stats(character)
                }
            }
        }
    }
}

@Composable
fun Stats(character: Character, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(24.dp), modifier = modifier) {
        Stat(character.edge) { EdgeIcon(modifier = it) }
        Stat(character.heart) { HeartIcon(modifier = it) }
        Stat(character.iron) { IronIcon(modifier = it) }
        Stat(character.shadow) { ShadowIcon(modifier = it) }
        Stat(character.wits) { WitsIcon(modifier = it) }
    }
}

@Composable
fun Stat(value: Int, icon: @Composable (modifier: Modifier) -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        icon(modifier = Modifier.size(24.dp))
        Text(value.toString(), fontSize = 24.sp)
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
