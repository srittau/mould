package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createProgress
import org.rittau.mould.model.ChallengeRank
import org.rittau.mould.model.Character
import org.rittau.mould.model.ProgressTrack
import org.rittau.mould.model.ProgressType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressEditorView(character: Character, onSave: (track: ProgressTrack) -> Unit, onClose: () -> Unit) {
    var closeDialog by rememberSaveable { mutableStateOf(false) }

    var name by rememberSaveable {
        mutableStateOf("")
    }
    var challengeRank by rememberSaveable {
        mutableStateOf(ChallengeRank.Troublesome)
    }
    var type by rememberSaveable {
        mutableStateOf(ProgressType.Other)
    }
    var notes by rememberSaveable {
        mutableStateOf("")
    }

    fun onSaveClick() {
        val track = runBlocking {
            createProgress(character.uuid, name, challengeRank, type, notes)
        }
        onSave(track)
    }

    if (closeDialog) {
        AlertDialog(
            onDismissRequest = { closeDialog = false },
            confirmButton = {
                Button(onClick = {
                    closeDialog = false
                    onClose()
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

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TopAppBar(title = {
            Text("Add Progress Track")
        }, navigationIcon = {
            IconButton(onClick = { closeDialog = true }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        })
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            TextField(value = name,
                label = { Text("Name") },
                singleLine = true,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
            )
            ChallengeSelect(challengeRank, onValueChange = { challengeRank = it })
            ProgressTypeSelect(type, onValueChange = { type = it })
            TextField(value = notes,
                label = { Text("Notes") },
                minLines = 5,
                onValueChange = { notes = it },
                modifier = Modifier.fillMaxWidth(),
            )
            Button(onClick = { onSaveClick() }) {
                Text(text = "Save")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTypeSelect(type: ProgressType, onValueChange: (ProgressType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            type.name,
            readOnly = true,
            singleLine = true,
            label = { Text("Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            onValueChange = {},
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
        )
        ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            for (pType in ProgressType.values()) {
                DropdownMenuItem(
                    text = { Text(pType.name) },
                    onClick = { onValueChange(pType); expanded = false },
                )
            }
        }
    }
}
