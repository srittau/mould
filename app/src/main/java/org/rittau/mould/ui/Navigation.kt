package org.rittau.mould.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import org.rittau.mould.model.StatOrTrack
import java.io.Serializable
import java.util.UUID

enum class MouldScreenType {
    CampaignList, CharacterSheet, CharacterEditor, Progress, ProgressEditor, Dice, Notes, WorldNote, WorldNoteEditor, JournalEntry, JournalEditor,
}

class MouldNavigation(
    private var controller: NavHostController,
    var currentScreen: MouldScreenType = MouldScreenType.CampaignList,
) : Serializable {
    var selectedCampaign by mutableStateOf<UUID?>(null)
    var selectedStat by mutableStateOf<StatOrTrack?>(null)
    var selectedProgressTrack by mutableStateOf<UUID?>(null)
    var selectedNote by mutableStateOf<UUID?>(null)
    var selectedJournal by mutableStateOf<UUID?>(null)
    var notesTab by mutableStateOf(0)

    fun updateScreen(
        newController: NavHostController,
        backStackEntry: androidx.navigation.NavBackStackEntry?,
    ) {
        controller = newController
        val route = backStackEntry?.destination?.route
        if (route != null) {
            currentScreen = MouldScreenType.valueOf(route)
        }
    }

    fun findCurrentScreen(screens: List<MouldScreen>): MouldScreen =
        screens.first { it.screen == currentScreen }

    fun onNavBarClick(type: MouldScreenType) {
        controller.navigate(type.name) {
            launchSingleTop = true
            restoreState = true
            popUpTo(controller.graph.findStartDestination().id) { saveState = true }
        }
    }

    fun onEditCampaign(uuid: UUID) {
        selectedCampaign = uuid
    }

    fun onCampaignAdded(uuid: UUID) {
        selectedCampaign = uuid
    }

    fun onCloseCampaignEditor() {
        selectedCampaign = null
    }

    fun onCampaignDeleted() {
        selectedCampaign = null
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
        controller.navigate(MouldScreenType.JournalEntry.name)
        controller.navigate(MouldScreenType.JournalEditor.name)
    }

    fun onEditJournalEntry() {
        if (selectedJournal == null) {
            throw IllegalStateException("No journal entry to edit")
        }
        controller.navigate(MouldScreenType.JournalEditor.name)
    }

    fun onJournalEntryClicked(uuid: UUID) {
        selectedJournal = uuid
        controller.navigate(MouldScreenType.JournalEntry.name)
    }

    fun onCloseJournalEntry() {
        controller.popBackStack()
        selectedJournal = null
    }

    fun onCloseJournalEditor() {
        controller.popBackStack()
    }

    fun onDeleteJournalEntry() {
        controller.popBackStack()
        controller.popBackStack()
        selectedJournal = null
    }
}

open class MouldScreen {
    open val screen: MouldScreenType = MouldScreenType.CampaignList

    open val hasAppBar: Boolean = false
    open fun title(): String = ""

    @Composable
    open fun NavigationIcon() {
    }

    @Composable
    open fun Content() {
    }

    @Composable
    open fun FloatingIcon() {
    }

    open val hasNavBar: Boolean = true
}
