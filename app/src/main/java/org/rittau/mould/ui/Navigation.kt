package org.rittau.mould.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import org.rittau.mould.model.StatOrTrack
import java.util.UUID

enum class MouldScreenType {
    CampaignList, CharacterSheet, CharacterEditor, Progress, ProgressEditor, Dice, Notes, WorldNote, WorldNoteEditor, JournalEditor,
}

class MouldNavigation(
    private val controller: NavHostController,
    var currentScreen: MouldScreenType = MouldScreenType.CampaignList,
) {
    var selectedStat by mutableStateOf<StatOrTrack?>(null)
    var selectedProgressTrack by mutableStateOf<UUID?>(null)
    var selectedNote by mutableStateOf<UUID?>(null)
    var selectedJournal by mutableStateOf<UUID?>(null)

    fun updateScreen(backStackEntry: androidx.navigation.NavBackStackEntry?) {
        val route = backStackEntry?.destination?.route
        if (route != null) {
            currentScreen = MouldScreenType.valueOf(route)
        }
    }

    fun onNavBarClick(type: MouldScreenType) {
        controller.navigate(type.name) {
            launchSingleTop = true
            restoreState = true
            popUpTo(controller.graph.findStartDestination().id) { saveState = true }
        }
    }

    fun onCharacterOpened() = controller.navigate(MouldScreenType.CharacterSheet.name)
    fun onEditCharacter() = controller.navigate(MouldScreenType.CharacterEditor.name)
    fun onCloseCharacterEditor() = controller.popBackStack()

    fun onStatOrTrackClicked(stat: StatOrTrack) {
        selectedStat = stat
        controller.navigate(MouldScreenType.Dice.name)
    }

    fun onBondsClicked() = controller.navigate(MouldScreenType.Notes.name)
    fun onProgressAdded(uuid: UUID) {
        selectedProgressTrack = uuid
        controller.navigate(MouldScreenType.ProgressEditor.name)
    }

    fun onProgressEditClick(uuid: UUID) {
        selectedProgressTrack = uuid
        controller.navigate(MouldScreenType.ProgressEditor.name)
    }

    fun onCloseProgressEditor() {
        controller.popBackStack()
        selectedProgressTrack = null
    }

    fun onProgressDeleted() {
        controller.popBackStack()
        selectedProgressTrack = null
    }

    fun onNoteClicked(uuid: UUID) {
        selectedNote = uuid
        controller.navigate(MouldScreenType.WorldNote.name)
    }

    fun onNoteAdded(uuid: UUID) {
        selectedNote = uuid
        controller.navigate(MouldScreenType.WorldNote.name)
        controller.navigate(MouldScreenType.WorldNoteEditor.name)
    }

    fun onEditNote() {
        if (selectedNote == null) {
            throw IllegalStateException("No note to edit")
        }
        controller.navigate(MouldScreenType.WorldNoteEditor.name)
    }

    fun onCloseNote() {
        controller.popBackStack()
        selectedNote = null
    }

    fun onCloseNoteEditor() = controller.popBackStack()
    fun onNoteDeleted() {
        controller.popBackStack()
        controller.popBackStack()
        selectedNote = null
    }

    fun onJournalEntryAdded(uuid: UUID) {
        selectedJournal = uuid
        controller.navigate(MouldScreenType.JournalEditor.name)
    }

    fun onJournalEntryClicked(uuid: UUID) {
        selectedJournal = uuid
        controller.navigate(MouldScreenType.JournalEditor.name)
    }

    fun onCloseJournalEditor() {
        controller.popBackStack()
        selectedJournal = null
    }

    fun onDeleteJournalEntry() {
        controller.popBackStack()
        selectedJournal = null
    }
}

interface MouldScreen {
    val screen: MouldScreenType

    @Composable
    fun Content()
}
