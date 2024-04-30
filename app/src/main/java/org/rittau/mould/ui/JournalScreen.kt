package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import dev.jeziellago.compose.markdowntext.MarkdownText
import org.rittau.mould.model.Character
import org.rittau.mould.model.JournalEntry
import org.rittau.mould.model.MouldModel
import java.util.UUID

class JournalScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen() {
    override val screen = MouldScreenType.JournalEntry

    override val hasAppBar: Boolean = true

    override fun title(): String {
        val entryUUID = navigation.selectedJournal ?: UUID.randomUUID()
        val entryTitle = model.findJournalEntry(entryUUID)?.title ?: ""
        return entryTitle.ifEmpty { "Untitled journal entry" }
    }

    @Composable
    override fun NavigationIcon() {
        IconButton(onClick = { navigation.onCloseJournalEntry() }) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }
    }

    @Composable
    override fun Content() {
        val entryUUID = navigation.selectedJournal ?: return
        val entry = model.findJournalEntry(entryUUID) ?: return
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .padding(8.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            if (entry.date.isNotEmpty()) {
                Text(
                    entry.date,
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            MarkdownText(entry.text, style = MaterialTheme.typography.bodyLarge)
        }
    }

    @Composable
    override fun FloatingIcon() {
        FloatingActionButton(onClick = { navigation.onEditJournalEntry() }) {
            Icon(Icons.Filled.Edit, contentDescription = "Edit")
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun JournalScreenScreenPreview() {
    val entryUUID = UUID.randomUUID()
    val entry = JournalEntry(
        entryUUID, "Title", "Some time in the future", "Text\nwith multiple\n\nlines"
    )
    val character = Character(UUID.randomUUID(), "Joe", bonds = setOf(entryUUID))
    val model = MouldModel()
    model.setCharacter(character, emptyList(), emptyList(), listOf(entry))
    val nav = MouldNavigation(NavHostController(LocalContext.current))
    nav.selectedJournal = entryUUID
    JournalScreen(model, nav).Content()
}
