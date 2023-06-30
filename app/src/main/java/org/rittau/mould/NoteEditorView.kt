package org.rittau.mould

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
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
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorView(note: CampaignNote, onClose: () -> Unit) {
    var title by rememberSaveable {
        mutableStateOf(note.title)
    }
    var text by rememberSaveable {
        mutableStateOf(note.text)
    }
    var changed by rememberSaveable { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = {
                Text(
                    if (title != "") title else "Untitled note",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Save")
                }
            },
            actions = {
                IconButton(onClick = {
                    runBlocking {
                        deleteNote(note)
                    }
                    onClose()
                }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            TextField(
                title,
                singleLine = true,
                onValueChange = { title = it; changed = true },
                modifier = Modifier.fillMaxWidth(),
            )
            TextField(
                text,
                minLines = 5,
                onValueChange = { text = it; changed = true },
                modifier = Modifier.weight(1f).fillMaxWidth(),
            )
            Button(enabled = changed, onClick = {
                note.title = title
                note.text = text
                runBlocking {
                    updateNote(note)
                }
                changed = false
            }) {
                Text("Save")
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NoteEditorViewPreview() {
    NoteEditorView(CampaignNote(UUID.randomUUID(), "Title", "Text\nwith multiple\n\nlines")) {}
}
