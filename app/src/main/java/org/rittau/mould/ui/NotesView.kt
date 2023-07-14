package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import org.rittau.mould.model.WorldNote
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

@Composable
fun NotesView(character: Character, onOpenNote: (WorldNote, Boolean) -> Unit, onOpenJournal: (CampaignNote) -> Unit) {
    var tab by rememberSaveable { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex = tab) {
            Tab(selected = tab == 0, text = { Text(text = "Notes") }, onClick = { tab = 0 })
            Tab(selected = tab == 1, text = { Text(text = "Journal") }, onClick = { tab = 1 })
        }
        when (tab) {
            0 -> NotesPage(character, onOpenNote)
            1 -> JournalPage(character, onOpenJournal)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesViewPreview() {
    val character = Character(UUID.randomUUID(), "Joe")
    MouldTheme {
        NotesView(character, { _, _ -> }, {})
    }
}
