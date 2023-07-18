package org.rittau.mould.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.IndeterminateCheckBox
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import org.rittau.mould.initializeDatabase
import org.rittau.mould.model.MouldModel
import org.rittau.mould.ui.theme.MouldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            initializeDatabase(applicationContext)
        }
        val model = MouldModel()

        setContent {
            Content(model)
        }
    }
}

@Composable
fun Content(model: MouldModel) {
    val navController: NavHostController = rememberNavController()
    val navigation = remember { MouldNavigation(navController) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        MouldScreen.valueOf(backStackEntry?.destination?.route ?: MouldScreen.CampaignList.name)

    MouldTheme {
        Scaffold(bottomBar = {
            if (currentScreen != MouldScreen.CampaignList) {
                NavBar(currentScreen) {
                    navController.navigate(it.name) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                    }
                }
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MouldScreen.CampaignList.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(MouldScreen.CampaignList.name) {
                    CampaignListView(model, navigation)
                }
                composable(MouldScreen.Character.name) {
                    CharacterSheet(model, navigation)
                }
                composable(MouldScreen.Progress.name) {
                    ProgressView(model, navigation)
                }
                composable(MouldScreen.ProgressEditor.name) {
                    ProgressEditorView(model, navigation)
                }
                composable(MouldScreen.CharacterEditor.name) {
                    CharacterEditor(model, navigation)
                }
                composable(MouldScreen.Dice.name) {
                    DiceView(model, navigation)
                }
                composable(MouldScreen.Notes.name) {
                    NotesView(model, navigation)
                }
                composable(MouldScreen.Note.name) {
                    NoteView(model, navigation)
                }
                composable(MouldScreen.NoteEditor.name) {
                    NoteEditorView(model, navigation)
                }
                composable(MouldScreen.JournalEditor.name) {
                    JournalEditorView(model, navigation)
                }
            }
        }
    }
}

@Composable
fun NavBar(activeView: MouldScreen, onChange: (MouldScreen) -> Unit) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Filled.Person, contentDescription = "Character") },
            label = { Text("Character") },
            selected = activeView == MouldScreen.Character,
            onClick = { onChange(MouldScreen.Character) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Filled.IndeterminateCheckBox, contentDescription = "Progress"
            )
        },
            label = { Text("Progress") },
            selected = activeView in arrayOf(MouldScreen.Progress, MouldScreen.ProgressEditor),
            onClick = { onChange(MouldScreen.Progress) })
        NavigationBarItem(icon = { Icon(Icons.Filled.Casino, contentDescription = "Dice") },
            label = { Text("Dice") },
            selected = activeView == MouldScreen.Dice,
            onClick = { onChange(MouldScreen.Dice) })
        NavigationBarItem(icon = { Icon(Icons.Filled.NoteAlt, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = activeView in arrayOf(
                MouldScreen.Notes, MouldScreen.Note, MouldScreen.NoteEditor
            ),
            onClick = { onChange(MouldScreen.Notes) })
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NavBarPreview() {
    MouldTheme {
        NavBar(MouldScreen.Character) {}
    }
}
