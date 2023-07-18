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
        MouldScreenType.valueOf(
            backStackEntry?.destination?.route ?: MouldScreenType.CampaignList.name
        )

    val screens: List<MouldScreen> = listOf(
        CampaignListScreen(model, navigation),
        CharacterSheetScreen(model, navigation),
        CharacterEditorScreen(model, navigation),
        ProgressScreen(model, navigation),
        ProgressEditorScreen(model, navigation),
        DiceScreen(model, navigation),
        NotesScreen(model, navigation),
        WorldNoteScreen(model, navigation),
        WorldNoteEditorScreen(model, navigation),
        JournalEditorScreen(model, navigation),
    )

    MouldTheme {
        Scaffold(bottomBar = {
            if (currentScreen != MouldScreenType.CampaignList) {
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
                startDestination = MouldScreenType.CampaignList.name,
                modifier = Modifier.padding(innerPadding),
            ) {
                screens.forEach { screen ->
                    composable(screen.screen.name) {
                        screen.Content()
                    }
                }
            }
        }
    }
}

@Composable
fun NavBar(activeView: MouldScreenType, onChange: (MouldScreenType) -> Unit) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Filled.Person, contentDescription = "Character") },
            label = { Text("Character") },
            selected = activeView == MouldScreenType.CharacterSheet,
            onClick = { onChange(MouldScreenType.CharacterSheet) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Filled.IndeterminateCheckBox, contentDescription = "Progress"
            )
        },
            label = { Text("Progress") },
            selected = activeView in arrayOf(
                MouldScreenType.Progress,
                MouldScreenType.ProgressEditor
            ),
            onClick = { onChange(MouldScreenType.Progress) })
        NavigationBarItem(icon = { Icon(Icons.Filled.Casino, contentDescription = "Dice") },
            label = { Text("Dice") },
            selected = activeView == MouldScreenType.Dice,
            onClick = { onChange(MouldScreenType.Dice) })
        NavigationBarItem(icon = { Icon(Icons.Filled.NoteAlt, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = activeView in arrayOf(
                MouldScreenType.Notes, MouldScreenType.WorldNote, MouldScreenType.WorldNoteEditor
            ),
            onClick = { onChange(MouldScreenType.Notes) })
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NavBarPreview() {
    MouldTheme {
        NavBar(MouldScreenType.CharacterSheet) {}
    }
}
