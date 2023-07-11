package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.rittau.mould.R
import org.rittau.mould.createBond
import org.rittau.mould.deleteBond
import org.rittau.mould.deleteWorldNote
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType
import org.rittau.mould.updateWorldNote
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorView(character: Character, note: WorldNote, onClose: () -> Unit) {
    var title by rememberSaveable {
        mutableStateOf(note.title)
    }
    var type by rememberSaveable {
        mutableStateOf(note.type)
    }
    var summary by rememberSaveable {
        mutableStateOf(note.summary)
    }
    var text by rememberSaveable {
        mutableStateOf(note.text)
    }
    var changed by rememberSaveable { mutableStateOf(false) }
    var bonded by rememberSaveable { mutableStateOf(character.isBondedTo(note.uuid)) }
    var closeDialog by rememberSaveable { mutableStateOf(false) }
    var deleteDialog by rememberSaveable { mutableStateOf(false) }

    fun onSave() {
        note.title = title
        note.type = type
        note.summary = summary
        note.text = text
        runBlocking {
            updateWorldNote(note)
        }
        changed = false
    }

    fun onFormBond() {
        CoroutineScope(Dispatchers.IO).launch {
            createBond(note.uuid)
        }
        character.forgeBond(note.uuid)
        bonded = true
    }

    fun onClearBond() {
        CoroutineScope(Dispatchers.IO).launch {
            deleteBond(note.uuid)
        }
        character.clearBond(note.uuid)
        bonded = false
    }

    fun onDelete() {
        runBlocking {
            deleteWorldNote(note)
        }
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
                    onDelete()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                Button(onClick = { deleteDialog = false }) {
                    Text("Cancel")
                }
            },
            text = { Text("Are you sure you want to delete this note?") },
        )
    }

    fun onCloseClick() {
        if (changed) {
            closeDialog = true
        } else {
            onClose()
        }
    }

    val displayTitle = if (title != "") title else "Untitled note"

    Column {
        TopAppBar(title = {
            Text(
                "Editing $displayTitle", maxLines = 1, overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = { onCloseClick() }) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Save")
            }
        }, actions = {
            IconButton(onClick = { deleteDialog = true }) {
                Icon(Icons.Filled.Delete, contentDescription = "Delete")
            }
        })

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            TextField(
                title,
                label = { Text("Title") },
                singleLine = true,
                onValueChange = { title = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
            NoteTypeSelect(type) { type = it; changed = true }
            TextField(
                summary,
                label = { Text("Summary") },
                singleLine = true,
                onValueChange = { summary = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                text,
                label = { Text("Text") },
                minLines = 5,
                onValueChange = { text = it; changed = true },
                modifier = Modifier
                    .height(300.dp)
                    .fillMaxWidth(),
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
            ) {
                if (bonded) {
                    OutlinedButton(onClick = { onClearBond() }) { Text("Clear bond") }
                } else {
                    OutlinedButton(onClick = { onFormBond() }) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(
                                painterResource(R.drawable.bond),
                                "Bonded",
                                modifier = Modifier.size(24.dp),
                            )
                            Text("Forge bond")
                        }
                    }
                }
                Button(enabled = changed, onClick = { onSave() }) {
                    Text("Save")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteTypeSelect(value: WorldNoteType, onChange: (WorldNoteType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        TextField(
            value.name,
            readOnly = true,
            singleLine = true,
            label = { Text("Type") },
            leadingIcon = { NoteTypeIcon(value, modifier = Modifier.size(24.dp)) },
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
            for (type in WorldNoteType.values()) {
                DropdownMenuItem(
                    text = { Text(type.name) },
                    leadingIcon = { NoteTypeIcon(type, modifier = Modifier.size(24.dp)) },
                    onClick = { onChange(type); expanded = false },
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NoteEditorViewPreview() {
    NoteEditorView(
        Character("Joe"), WorldNote(
            UUID.randomUUID(),
            "Title",
            WorldNoteType.World,
            "Summary",
            "Text\nwith multiple\n\nlines"
        )
    ) {}
}
