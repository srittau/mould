package org.rittau.mould.ui

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.rittau.mould.model.Character
import org.rittau.mould.model.ConditionTrack
import org.rittau.mould.model.MAX_STATUS
import org.rittau.mould.model.MIN_STATUS
import org.rittau.mould.saveCharacter
import org.rittau.mould.ui.theme.MouldTheme

@Composable
fun ConditionTrack(
    icon: @Composable (modifier: Modifier) -> Unit,
    character: Character,
    track: ConditionTrack,
) {
    var value by rememberSaveable {
        mutableStateOf(track.value)
    }

    fun onLose() {
        value = track.lose()
        CoroutineScope(Dispatchers.IO).launch {
            saveCharacter(character)
        }
    }

    fun onGain() {
        value = track.gain()
        CoroutineScope(Dispatchers.IO).launch {
            saveCharacter(character)
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        icon(modifier = Modifier.size(24.dp))
        IconButton(enabled = track.canLose, onClick = { onLose() }) {
            Icon(Icons.Filled.Remove, "Lose", tint = MaterialTheme.colorScheme.secondary)
        }
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .border(
                    .5.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(4.dp),
                )
                .clip(RoundedCornerShape(4.dp)),
        ) {
            for (v in MIN_STATUS..MAX_STATUS) {
                ConditionItem(v, selected = value == v, last = v == MAX_STATUS)
            }
        }
        IconButton(enabled = track.canGain, onClick = { onGain() }) {
            Icon(Icons.Filled.Add, "Gain", tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ConditionTrackPreview() {
    val character = Character("", health = 3)
    MouldTheme {
        ConditionTrack({ HealthIcon(modifier = it) }, character, character.healthTrack)
    }
}
