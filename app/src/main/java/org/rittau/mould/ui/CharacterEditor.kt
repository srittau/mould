package org.rittau.mould.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rittau.mould.model.Character
import org.rittau.mould.saveCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterEditor(character: Character, onBack: () -> Unit) {
    var name by rememberSaveable {
        mutableStateOf(character.name)
    }
    var closeDialog by rememberSaveable { mutableStateOf(false) }

    val titleName = name.ifBlank { "Unnamed Character" }

    fun onClose() {
        if (name != character.name) {
            closeDialog = true
        } else {
            onBack()
        }
    }

    if (closeDialog) {
        AlertDialog(
            onDismissRequest = { closeDialog = false },
            confirmButton = {
                Button(onClick = {
                    closeDialog = false
                    onBack()
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
                IconButton(onClick =  { onClose() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = {
                    character.name = name
                    CoroutineScope(Dispatchers.IO).launch {
                        saveCharacter(character)
                    }
                    onBack()
                }) {
                    Icon(Icons.Filled.Done, contentDescription = "Save")
                }
            },
        )
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(value = name, onValueChange = { name = it })
        }
    }
}
