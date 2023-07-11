package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.deleteCampaignNote
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.updateCampaignNote
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEditorView(note: CampaignNote, onClose: () -> Unit) {
    var title by rememberSaveable {
        mutableStateOf(note.title)
    }
    var date by rememberSaveable {
        mutableStateOf(note.date)
    }
    var text by rememberSaveable {
        mutableStateOf(note.text)
    }
    var changed by rememberSaveable { mutableStateOf(false) }
    var deleteDialog by rememberSaveable { mutableStateOf(false) }

    fun onSave() {
        note.title = title
        note.date = date
        note.text = text
        runBlocking {
            updateCampaignNote(note)
        }
        changed = false
    }

    fun onDelete() {
        runBlocking {
            deleteCampaignNote(note)
        }
        onClose()
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
            title = { Text(text = "Delete journal entry?") },
            text = { Text("Are you sure you want to delete this journal entry?") },
        )
    }

    Column {
        TopAppBar(title = {
            Text(
                if (title != "") title else "Untitled journal entry",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }, navigationIcon = {
            IconButton(onClick = onClose) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Save")
            }
        }, actions = {
            Button(onClick = { onSave() }, enabled = changed) {
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
                .padding(8.dp),
        ) {
            TextField(
                title,
                label = { Text("Title") },
                singleLine = true,
                onValueChange = { title = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                date,
                label = { Text("Date") },
                singleLine = true,
                onValueChange = { date = it; changed = true },
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
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JournalEditorViewPreview() {
    JournalEditorView(
        CampaignNote(
            UUID.randomUUID(), "Title", "Some date", "Text\nwith multiple\n\nlines"
        )
    ) {}
}
