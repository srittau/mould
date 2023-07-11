package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.runBlocking
import org.rittau.mould.model.Character
import org.rittau.mould.model.ProgressTrack
import org.rittau.mould.model.addProgress
import org.rittau.mould.ui.theme.MouldTheme
import org.rittau.mould.updateProgress
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressView(
    character: Character,
    progress: List<ProgressTrack>,
    onBondsClick: () -> Unit,
    onAddProgress: () -> Unit,
) {
    val name = character.name.ifBlank { "Unnamed Character" }
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        TopAppBar(
            title = { Text(text = name) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        )
        BondsSection(character, onBondsClick)
        ProgressList(progress, modifier = Modifier.padding(bottom = 60.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(modifier = Modifier
            .padding(16.dp)
            .align(Alignment.BottomEnd),
            onClick = { onAddProgress() }) {
            Icon(Icons.Filled.Add, "Add progress track")
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressViewPreview() {
    val character = Character("Joe", bonds = setOf(UUID.randomUUID(), UUID.randomUUID()))
    MouldTheme {
        ProgressView(character, listOf(), {}, {})
    }
}

@Composable
fun BondsSection(character: Character, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Text("Bonds", style = MaterialTheme.typography.labelLarge)
        ProgressBar(filled = character.bondCount)
    }

}

@Composable
fun ProgressList(progress: List<ProgressTrack>, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
    ) {
        Text("Progress", style = MaterialTheme.typography.labelLarge)
        progress.forEach {
            ProgressTrack(it)
        }
    }
}

@Composable
fun ProgressTrack(track: ProgressTrack) {
    val progress = rememberSaveable {
        mutableStateOf(track.progress)
    }

    fun onAdd() {
        progress.value = addProgress(progress.value, track.challengeRank)
        track.progress = progress.value
        runBlocking {
            updateProgress(track)
        }
    }

    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(track.name, style = MaterialTheme.typography.labelMedium)
            ChallengeIndicator(rank = track.challengeRank)
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            ProgressBar(filled = progress.value, modifier = Modifier.align(Alignment.Center))
            IconButton(onClick = { onAdd() }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Filled.Add, "Add progress")
            }
        }
        Text(track.notes, style = MaterialTheme.typography.bodySmall)
    }
}
