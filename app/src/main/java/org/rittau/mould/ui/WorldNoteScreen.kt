package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.rittau.mould.R
import org.rittau.mould.model.Character
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.WorldNote
import org.rittau.mould.model.WorldNoteType
import java.util.UUID

class WorldNoteScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen {
    override val screen = MouldScreenType.WorldNote

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val character = model.character
        val noteUUID = navigation.selectedNote ?: return
        val note = model.findWorldNote(noteUUID) ?: return
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            if (note.title != "") note.title else "Untitled note",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigation.onCloseNote() }) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { navigation.onEditNote() }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
            },
        ) { contentPadding ->
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(contentPadding)
                    .padding(8.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                if (note.summary.isNotEmpty()) {
                    Text(
                        note.summary,
                        textAlign = TextAlign.Center,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    NoteTypeIcon(note.type, modifier = Modifier.size(18.dp))
                    Text(note.type.name, style = MaterialTheme.typography.titleMedium)
                }
                if (character.isBondedTo(note.uuid)) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painterResource(R.drawable.bond),
                            "Bonded",
                            modifier = Modifier.size(18.dp),
                        )
                        Text("Bonded", style = MaterialTheme.typography.titleMedium)
                    }
                }
                MarkdownText(note.text, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun WorldNoteScreenPreview() {
    val noteUUID = UUID.randomUUID()
    val note = WorldNote(
        noteUUID, "Title", WorldNoteType.World, "Summary", "Text\nwith multiple\n\nlines"
    )
    val character = Character(UUID.randomUUID(), "Joe", bonds = setOf(noteUUID))
    val model = MouldModel()
    model.setCharacter(character, emptyList(), listOf(note), emptyList())
    val nav = MouldNavigation(NavHostController(LocalContext.current))
    nav.selectedNote = noteUUID
    WorldNoteScreen(model, nav).Content()
}
