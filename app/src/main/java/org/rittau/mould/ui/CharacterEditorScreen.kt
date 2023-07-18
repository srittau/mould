package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.statsOk
import org.rittau.mould.saveCharacterSync

class CharacterEditorScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen {
    override val screen = MouldScreenType.CharacterEditor

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val character = model.character
        var name by rememberSaveable { mutableStateOf(character.name) }
        var summary by rememberSaveable { mutableStateOf(character.summary) }
        val edge = rememberSaveable { mutableStateOf(character.edge) }
        val heart = rememberSaveable { mutableStateOf(character.heart) }
        val iron = rememberSaveable { mutableStateOf(character.iron) }
        val shadow = rememberSaveable { mutableStateOf(character.shadow) }
        val wits = rememberSaveable { mutableStateOf(character.wits) }
        var closeDialog by rememberSaveable { mutableStateOf(false) }

        val titleName = name.ifBlank { "Unnamed Character" }

        fun onClose() {
            if (name != character.name || summary != character.summary || edge.value != character.edge || heart.value != character.heart || iron.value != character.iron || shadow.value != character.shadow || wits.value != character.wits) {
                closeDialog = true
            } else {
                navigation.onCloseCharacterEditor()
            }
        }

        fun save() {
            character.name = name
            character.summary = summary
            character.edge = edge.value
            character.heart = heart.value
            character.iron = iron.value
            character.shadow = shadow.value
            character.wits = wits.value
            saveCharacterSync(character)
        }

        if (closeDialog) {
            AlertDialog(
                onDismissRequest = { closeDialog = false },
                confirmButton = {
                    Button(onClick = {
                        closeDialog = false
                        navigation.onCloseCharacterEditor()
                    }) {
                        Text("Close")
                    }
                },
                dismissButton = {
                    Button(onClick = { closeDialog = false }) {
                        Text("Cancel")
                    }
                },
                text = { Text("Close without saving?") },
            )
        }


        Column {
            TopAppBar(
                title = { Text(text = "Edit $titleName") },
                navigationIcon = {
                    IconButton(onClick = { onClose() }) {
                        Icon(Icons.Filled.Close, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        save()
                        navigation.onCloseCharacterEditor()
                    }) {
                        Icon(Icons.Filled.Done, contentDescription = "Save")
                    }
                },
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)
            ) {
                TextField(
                    label = { Text(text = "Name") },
                    value = name,
                    singleLine = true,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextField(
                    label = { Text(text = "Short description") },
                    value = summary,
                    singleLine = true,
                    onValueChange = { summary = it }, modifier = Modifier.fillMaxWidth(),
                )
                Divider()
                StatField("Edge", edge) { EdgeIcon(modifier = it) }
                StatField("Heart", heart) { HeartIcon(modifier = it) }
                StatField("Iron", iron) { IronIcon(modifier = it) }
                StatField("Shadow", shadow) { ShadowIcon(modifier = it) }
                StatField("Wits", wits) { WitsIcon(modifier = it) }
                if (!statsOk(edge.value, heart.value, iron.value, shadow.value, wits.value)) {
                    Text(
                        "You should have one stat with 3, two stats with 2, and two stats with 1.",
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun StatField(statName: String, value: MutableState<Int>, icon: @Composable (modifier: Modifier) -> Unit) {
    var text by rememberSaveable { mutableStateOf(value.value.toString()) }

    TextField(
        value = text,
        label = { Text(text = statName) },
        trailingIcon = { icon(modifier = Modifier.size(26.dp)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, autoCorrect = false),
        modifier = Modifier.width(120.dp),
        onValueChange = {
            text = it
            if (it.toIntOrNull() != null) {
                value.value = it.toInt()
            }
        },
    )
}
