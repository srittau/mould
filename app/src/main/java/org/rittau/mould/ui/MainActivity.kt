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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.runBlocking
import org.rittau.mould.initializeDatabase
import org.rittau.mould.loadCampaigns
import org.rittau.mould.model.MouldModel
import org.rittau.mould.ui.theme.MouldTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runBlocking {
            initializeDatabase(applicationContext)
        }

        setContent {
            Content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Content() {
    val model = rememberSaveable { MouldModel() }
    loadCampaigns(model)

    val navController: NavHostController = rememberNavController()
    val navigation = rememberSaveable { MouldNavigation(navController) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    navigation.updateScreen(navController, backStackEntry)

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

    val screen = navigation.findCurrentScreen(screens)

    MouldTheme {
        Scaffold(topBar = {
            if (screen.hasAppBar) {
                TopAppBar(
                    title = {
                        Text(screen.title(), maxLines = 1, overflow = TextOverflow.Ellipsis)
                    },
                    navigationIcon = { screen.NavigationIcon() },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                )
            }
        }, floatingActionButton = { screen.FloatingIcon() }, bottomBar = {
            if (screen.hasNavBar) {
                NavBar(navigation)
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
fun NavBar(navigation: MouldNavigation) {
    NavigationBar {
        NavigationBarItem(icon = { Icon(Icons.Filled.Person, contentDescription = "Character") },
            label = { Text("Character") },
            selected = navigation.currentScreen == MouldScreenType.CharacterSheet,
            onClick = { navigation.onNavBarClick(MouldScreenType.CharacterSheet) })
        NavigationBarItem(icon = {
            Icon(
                Icons.Filled.IndeterminateCheckBox, contentDescription = "Progress"
            )
        }, label = { Text("Progress") }, selected = navigation.currentScreen in arrayOf(
            MouldScreenType.Progress, MouldScreenType.ProgressEditor
        ), onClick = { navigation.onNavBarClick(MouldScreenType.Progress) })
        NavigationBarItem(icon = { Icon(Icons.Filled.Casino, contentDescription = "Dice") },
            label = { Text("Dice") },
            selected = navigation.currentScreen == MouldScreenType.Dice,
            onClick = { navigation.onNavBarClick(MouldScreenType.Dice) })
        NavigationBarItem(icon = { Icon(Icons.Filled.NoteAlt, contentDescription = "Notes") },
            label = { Text("Notes") },
            selected = navigation.currentScreen in arrayOf(
                MouldScreenType.Notes, MouldScreenType.WorldNote, MouldScreenType.WorldNoteEditor
            ),
            onClick = { navigation.onNavBarClick(MouldScreenType.Notes) })
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun NavBarPreview() {
    val nav =
        MouldNavigation(NavHostController(LocalContext.current), MouldScreenType.CharacterSheet)
    MouldTheme {
        NavBar(nav)
    }
}
