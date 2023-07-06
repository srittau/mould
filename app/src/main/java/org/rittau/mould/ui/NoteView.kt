package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.rittau.mould.R
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteView(
    character: Character, note: WorldNote, onEdit: (note: WorldNote) -> Unit, onClose: () -> Unit
) {
    Column {
        TopAppBar(
            title = {
                Text(
                    if (note.title != "") note.title else "Untitled note",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            navigationIcon = {
                IconButton(onClick = onClose) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(onClick = { onEdit(note) }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        ) {
            if (character.isBondedTo(note.uuid)) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(R.drawable.bond), null, modifier = Modifier.size(24.dp),
                    )
                    Text("Bonded", style = MaterialTheme.typography.titleLarge)
                }
            }
            MarkdownText(note.text, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NoteViewPreview() {
    val noteUUID = UUID.randomUUID()
    NoteView(Character("Joe", bonds = setOf(noteUUID)),
        WorldNote(noteUUID, "Title", "Summary", "Text\nwith multiple\n\nlines"),
        {},
        {})
}
