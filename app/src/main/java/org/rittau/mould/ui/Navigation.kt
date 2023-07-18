package org.rittau.mould.ui

import androidx.compose.runtime.mutableStateOf
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.rittau.mould.model.StatOrTrack
import java.util.UUID

enum class MouldScreen {
    CampaignList, Character, CharacterEditor, Progress, ProgressEditor, Dice, Notes, Note, NoteEditor, JournalEditor,
}

class MouldNavigation(private val controller: NavHostController) {
    var selectedStat by mutableStateOf<StatOrTrack?>(null)
    var selectedProgressTrack by mutableStateOf<UUID?>(null)
    var selectedNote by mutableStateOf<UUID?>(null)
    var selectedJournal by mutableStateOf<UUID?>(null)

    fun onCharacterOpened() = controller.navigate(MouldScreen.Character.name)
    fun onEditCharacter() = controller.navigate(MouldScreen.CharacterEditor.name)
    fun onCloseCharacterEditor() = controller.popBackStack()

    fun onStatOrTrackClicked(stat: StatOrTrack) {
        selectedStat = stat
        controller.navigate(MouldScreen.Dice.name)
    }
    fun onBondsClicked() = controller.navigate(MouldScreen.Notes.name)
    fun onProgressAdded(uuid: UUID) {
        selectedProgressTrack = uuid
        controller.navigate(MouldScreen.ProgressEditor.name)
    }
    fun onProgressEditClick(uuid: UUID) {
        selectedProgressTrack = uuid
        controller.navigate(MouldScreen.ProgressEditor.name)
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
        controller.navigate(MouldScreen.Note.name)
    }

    fun onNoteAdded(uuid: UUID) {
        selectedNote = uuid
        controller.navigate(MouldScreen.Note.name)
        controller.navigate(MouldScreen.NoteEditor.name)
    }

    fun onEditNote() {
        if (selectedNote == null) {
            throw IllegalStateException("No note to edit")
        }
        controller.navigate(MouldScreen.NoteEditor.name)
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
        controller.navigate(MouldScreen.JournalEditor.name)
    }

    fun onJournalEntryClicked(uuid: UUID) {
        selectedJournal = uuid
        controller.navigate(MouldScreen.JournalEditor.name)
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
