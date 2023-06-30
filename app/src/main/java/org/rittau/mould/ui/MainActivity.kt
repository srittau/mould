package org.rittau.mould.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import org.rittau.mould.loadCharacter
import org.rittau.mould.model.CampaignNote
import org.rittau.mould.model.Character
import org.rittau.mould.ui.theme.MouldTheme

enum class MouldScreen {
    Character, Dice, Notes, NoteEditor,
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val character = runBlocking {
            initializeDatabase(applicationContext)
            loadCharacter()
        }

        setContent {
            Content(character)
        }
    }
}

@Composable
fun Content(character: Character) {
    val navController: NavHostController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen =
        MouldScreen.valueOf(backStackEntry?.destination?.route ?: MouldScreen.Character.name)
    val currentNote = rememberSaveable {
        mutableStateOf<CampaignNote?>(null)
    }

    MouldTheme {
        Scaffold(bottomBar = {
            NavBar(currentScreen) {
                navController.navigate(it.name) {
                    launchSingleTop = true
                    restoreState = true
                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                }
            }
        }) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = MouldScreen.Character.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                composable(MouldScreen.Character.name) {
                    CharacterSheet(character)
                }
                composable(MouldScreen.Dice.name) {
                    DiceView()
                }
                composable(MouldScreen.Notes.name) {
                    NotesView {
                        currentNote.value = it
                        navController.navigate(MouldScreen.NoteEditor.name)
                    }
                }
                composable(MouldScreen.NoteEditor.name) {
                    val note = currentNote.value
                    if (note != null) {
                        NoteEditorView(note) {
                            navController.popBackStack()
                            currentNote.value = null
                        }
                    }
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
        NavigationBarItem(icon = { Icon(Icons.Filled.Casino, contentDescription = "Dice") },
            label = { Text("Dice") },
            selected = activeView == MouldScreen.Dice,
            onClick = { onChange(MouldScreen.Dice) })
        NavigationBarItem(icon = { Icon(Icons.Filled.NoteAlt, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = activeView in arrayOf(MouldScreen.Notes, MouldScreen.NoteEditor),
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
