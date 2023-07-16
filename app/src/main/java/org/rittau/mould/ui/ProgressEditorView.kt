package org.rittau.mould.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import org.rittau.mould.deleteProgress
import org.rittau.mould.updateProgress
import org.rittau.mould.model.ProgressTrack
import org.rittau.mould.model.ProgressType
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressEditorView(track: ProgressTrack, onClose: () -> Unit, onDelete: (uuid: UUID) -> Unit) {
    var closeDialog by rememberSaveable { mutableStateOf(false) }
    var deleteDialog by rememberSaveable { mutableStateOf(false) }

    var changed by rememberSaveable {
        mutableStateOf(false)
    }
    var name by rememberSaveable {
        mutableStateOf(track.name)
    }
    var challengeRank by rememberSaveable {
        mutableStateOf(track.challengeRank)
    }
    var type by rememberSaveable {
        mutableStateOf(track.type)
    }
    var notes by rememberSaveable {
        mutableStateOf(track.notes)
    }

    fun onCloseClick() {
        if (changed) {
            closeDialog = true
        } else {
            onClose()
        }
    }

    fun onSaveClick() {
        track.name = name
        track.challengeRank = challengeRank
        track.type = type
        track.notes = notes
        runBlocking { updateProgress(track) }
        changed = false
    }

    fun onDeleteClick() {
        runBlocking { deleteProgress(track.uuid) }
        onDelete(track.uuid)
        onClose()
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

    if (deleteDialog) {
        AlertDialog(
            onDismissRequest = { deleteDialog = false },
            confirmButton = {
                Button(onClick = {
                    deleteDialog = false
                    onDeleteClick()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { deleteDialog = false }) {
                    Text("Cancel")
                }
            },
            text = { Text("Are you sure you want to delete this progress track?") },
        )
    }

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TopAppBar(title = {
            Text("Edit Progress Track")
        }, navigationIcon = {
            IconButton(onClick = { onCloseClick() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
            }
        }, actions = {
            Button(onClick = { onSaveClick() }, enabled = changed) {
                Text("Save")
            }
            IconButton(onClick = { deleteDialog = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        })
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            TextField(
                value = name,
                label = { Text("Name") },
                singleLine = true,
                onValueChange = { name = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
            ChallengeSelect(challengeRank, onValueChange = { challengeRank = it; changed = true })
            ProgressTypeSelect(type, onValueChange = { type = it; changed = true })
            TextField(
                value = notes,
                label = { Text("Notes") },
                minLines = 5,
                onValueChange = { notes = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
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
