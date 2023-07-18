package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import kotlinx.coroutines.runBlocking
import org.rittau.mould.createCampaignNote
import org.rittau.mould.createWorldNote
import org.rittau.mould.model.Character
import org.rittau.mould.model.MouldModel
import org.rittau.mould.model.WorldNoteType
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

class NotesScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen() {
    override val screen = MouldScreenType.Notes

    @Composable
    override fun Content() {
        val tab = navigation.notesTab
        Column {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0,
                    text = { Text(text = "Notes") },
                    onClick = { navigation.notesTab = 0 })
                Tab(selected = tab == 1,
                    text = { Text(text = "Journal") },
                    onClick = { navigation.notesTab = 1 })
            }
            when (tab) {
                0 -> NotesList(model, navigation)
                1 -> JournalList(model, navigation)
            }
        }
    }

    @Composable
    override fun FloatingIcon() {
        fun onClick() {
            when (navigation.notesTab) {
                0 -> {
                    val note = runBlocking {
                        createWorldNote("", WorldNoteType.Other, "", "")
                    }
                    model.addWorldNote(note)
                    navigation.onNoteAdded(note.uuid)
                }

                1 -> {
                    val entry = runBlocking {
                        createCampaignNote(model.character.uuid)
                    }
                    model.addJournalEntry(entry)
                    navigation.onJournalEntryAdded(entry.uuid)
                }
            }
        }

        val label = when (navigation.notesTab) {
            0 -> "Add note"
            1 -> "Add journal entry"
            else -> ""
        }

        FloatingActionButton(onClick = { onClick() }) {
            Icon(Icons.Filled.Add, label)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreview() {
    val model = MouldModel()
    model.setCharacter(
        Character(UUID.randomUUID(), "Joe"), emptyList(), emptyList(), emptyList()
    )
    val nav = MouldNavigation(NavHostController(LocalContext.current))
    MouldTheme {
        NotesScreen(model, nav).Content()
    }
}
