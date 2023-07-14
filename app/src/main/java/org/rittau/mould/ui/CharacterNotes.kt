package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.rittau.mould.model.Character
import org.rittau.mould.saveCharacterSync
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

@Composable
fun CharacterNotes(character: Character) {
    var notes by rememberSaveable {
        mutableStateOf(character.notes)
    }
    var editing by rememberSaveable {
        mutableStateOf(false)
    }

    fun onSave() {
        character.notes = notes
        saveCharacterSync(character)
        editing = false
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                "Notes and Inventory",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
            )
            if (editing) {
                Button(onClick = { onSave() }) {
                    Text("Save")
                }
            } else {
                IconButton(onClick = { editing = true }) {
                    Icon(
                        Icons.Filled.Edit,
                        contentDescription = "Edit",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
        if (editing) {
            TextField(
                value = notes,
                onValueChange = { notes = it },
                minLines = 5,
                maxLines = 5,
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            MarkdownText(notes, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun CharacterNotesPreview() {
    MouldTheme {
        CharacterNotes(
            Character(
                UUID.randomUUID(),
                "John",
                notes = "This is multiline text.\n\nWith *markdown*.\n\nAnd **formatting**.\n\nAnd [links](https://example.com)."
            )
        )
    }
}
