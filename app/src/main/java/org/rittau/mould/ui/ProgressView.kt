package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.runBlocking
import org.rittau.mould.model.Character
import org.rittau.mould.model.ProgressTrack
import org.rittau.mould.model.ProgressType
import org.rittau.mould.ui.theme.MouldTheme
import org.rittau.mould.updateProgress
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressView(
    character: Character,
    progress: List<ProgressTrack>,
    onBondsClick: () -> Unit,
    onProgressClick: (uuid: UUID) -> Unit,
    onAddProgress: () -> Unit,
) {
    val name = character.name.ifBlank { "Unnamed Character" }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = name) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAddProgress() }) {
                Icon(Icons.Filled.Add, "Add progress track")
            }
        }) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
                .verticalScroll(rememberScrollState())
        ) {
            BondsSection(character.bondsTrack, onBondsClick)
            ProgressList(
                progress,
                modifier = Modifier.padding(bottom = 60.dp),
                onProgressClick = onProgressClick
            )
        }
    }
}

@Composable
fun ProgressList(
    progress: List<ProgressTrack>,
    modifier: Modifier = Modifier,
    onProgressClick: (uuid: UUID) -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Text("Progress", style = MaterialTheme.typography.labelLarge)
        progress.forEach { track ->
            ProgressTrack(track, onEdit = { onProgressClick(track.uuid) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTrack(track: ProgressTrack, onEdit: () -> Unit) {
    var menuExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val progress =
        rememberSaveable {
            mutableStateOf(track.ticks)
        }

    fun onAdd() {
        progress.value = track.markProgress()
        runBlocking {
            updateProgress(track)
        }
    }

    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.align(Alignment.Center)
            ) {
                if (track.type != ProgressType.Other) {
                    Text(
                        "${track.type.name}:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(track.name, style = MaterialTheme.typography.labelMedium)
                ChallengeIndicator(rank = track.challengeRank)
            }
            ExposedDropdownMenuBox(
                expanded = menuExpanded,
                onExpandedChange = { menuExpanded = it },
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                IconButton(onClick = {}) {
                    Icon(Icons.Filled.MoreVert, "Menu", modifier = Modifier.menuAnchor())
                }
                ExposedDropdownMenu(
                    expanded = menuExpanded,
                    modifier = Modifier.width(200.dp),
                    onDismissRequest = { menuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = { Text("Edit progress track") },
                        leadingIcon = { Icon(Icons.Filled.Edit, null) },
                        onClick = { onEdit(); menuExpanded = false },
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth()) {
            ProgressBar(filled = progress.value, modifier = Modifier.align(Alignment.Center))
            IconButton(onClick = { onAdd() }, modifier = Modifier.align(Alignment.CenterEnd)) {
                Icon(Icons.Filled.Add, "Add progress")
            }
        }
        MarkdownText(
            track.notes,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ProgressViewPreview() {
    val character =
        Character(UUID.randomUUID(), "Joe", bonds = setOf(UUID.randomUUID(), UUID.randomUUID()))
    MouldTheme {
        ProgressView(character, listOf(), {}, {}, {})
    }
}
