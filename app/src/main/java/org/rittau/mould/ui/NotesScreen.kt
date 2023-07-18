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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import org.rittau.mould.model.Character
import org.rittau.mould.model.MouldModel
import org.rittau.mould.ui.theme.MouldTheme
import java.util.UUID

class NotesScreen(val model: MouldModel, val navigation: MouldNavigation) : MouldScreen {
    override val screen = MouldScreenType.Notes

    @Composable
    override fun Content() {
        var tab by rememberSaveable { mutableStateOf(0) }

        Column {
            TabRow(selectedTabIndex = tab) {
                Tab(selected = tab == 0, text = { Text(text = "Notes") }, onClick = { tab = 0 })
                Tab(selected = tab == 1, text = { Text(text = "Journal") }, onClick = { tab = 1 })
            }
            when (tab) {
                0 -> NotesPage(model, navigation)
                1 -> JournalPage(model, navigation)
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NotesScreenPreview() {
    val model = MouldModel()
    model.setCharacter(Character(UUID.randomUUID(), "Joe"), emptyList(), emptyList(), emptyList())
    val nav = MouldNavigation(NavHostController(LocalContext.current))
    MouldTheme {
        NotesScreen(model, nav).Content()
    }
}
